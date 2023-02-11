import { createApp } from "vue";
import { createPinia } from "pinia";
import "@mdi/font/css/materialdesignicons.css";
import httpClient from "@/utils/http.util.js";

// Vuetify
import "vuetify/styles";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { VDataTableServer } from "vuetify/labs/components";
import AdministrationPage from "@/pages/AdministrationPage.vue";
import Keycloak from "keycloak-js";
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

const vuetify = createVuetify({
  components: {
    VDataTableServer,
    ...components,
  },
  defaults: {
    VDataTableServer: {
      fixedHeader: true,
      noDataText: "Results not found",
    },
  },
  directives,
});

let keycloakConfig = {
  url: "http://127.0.0.1:8080/",
  realm: "BRK",
  clientId: "brk-carshare-client",
  onLoad: "login-required",
};

let keycloak = new Keycloak(keycloakConfig);

keycloak
  .init({
    onLoad: "login-required",
    pkceMethod: "S256",
    checkLoginIframe: false,
  })
  .then(async (authenticated) => {
    if (!authenticated) {
      window.location.reload();
    } else {
      console.log(keycloak);
      if (!keycloak.realmAccess.roles.includes("ADMIN")) {
        window.location.href = "/";
      } else {
        configureHttpClientInterceptors(keycloak);
        const app = createApp(AdministrationPage);
        app.config.globalProperties.$http = httpClient;
        app.config.globalProperties.$window = window;
        app.config.globalProperties.$keycloak = keycloak;
        app.use(createPinia());
        app.use(vuetify);
        app.component('Datepicker', Datepicker)

        app.mount("#administration");

        //Token Refresh
        setInterval(() => {
          keycloak
            .updateToken(70)
            .then((refreshed) => {
              if (refreshed) {
                console.log(
                  "Token refreshed, valid for  " +
                    Math.round(
                      keycloak.tokenParsed.exp +
                        keycloak.timeSkew -
                        new Date().getTime() / 1000
                    ) +
                    " seconds"
                );
              }
            })
            .catch((error) => {
              console.log("Failed to refresh token " + error);
            });
        }, 6000);
      }
    }
  })
  .catch((error) => {
    console.log("KeyCloak OnInitError " + error);
  });

function configureHttpClientInterceptors(keycloak) {
  const authInterceptor = (config) => {
    config.headers.Authorization = `Bearer ${keycloak.token}`;
    config.headers.Pragma = "no-cache";
    return config;
  };
  httpClient.interceptors.request.use(authInterceptor);
  httpClient.interceptors.response.use(
    (response) => {
      return response;
    },
    (error) => {
      let data = error.response.data;
      if (typeof data == "object" && data.Message) data = data.Message;
      console.error("Error: " + error + "\nData: " + data);
      return Promise.reject(error);
    }
  );
}
