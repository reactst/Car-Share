<template>
  <v-app :theme="theme">
    <v-navigation-drawer :width="300" permanent>
      <v-list>
        <v-list-item
          :prepend-avatar="`https://api.dicebear.com/5.x/bottts-neutral/svg?seed=${user.name}`"
          :title="user.name"
          :subtitle="user.email"
          ><template v-slot:append>
            <v-btn
              class="ml-5"
              :ripple="false"
              variant="plain"
              @click="onClick"
              :icon="
                theme === 'light' ? 'mdi-weather-sunny' : 'mdi-weather-night'
              "
            ></v-btn>
          </template>
        </v-list-item>
      </v-list>

      <v-divider></v-divider>
      <v-list
        density="compact"
        nav
        @click:select="changeView"
      >
        <v-list-item
          prepend-icon="mdi-car"
          title="Vehicles"
          value="VehiclesView"
        ></v-list-item>
        <v-list-item
          prepend-icon="mdi-file-document-alert-outline"
          title="Tickets"
          value="TicketsView"
        ></v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <component :is="selectedView" :theme="theme" />
    </v-main>
  </v-app>
</template>

<script>
import { ref } from "vue";
import VehiclesView from "@/views/VehiclesView.vue";
import TicketsView from "@/views/TicketsView.vue";
export default {
  data() {
    return {
      theme: ref("dark"),
      selectedView: "VehiclesView",
      user: { name: "", email: "" },
    };
  },
  async mounted() {
    this.user = await this.$keycloak.loadUserInfo();
  },
  components: {
    VehiclesView,
    TicketsView
  },
  methods: {
    onClick() {
      this.theme = this.theme === "light" ? "dark" : "light";
    },
    changeView(event) {
      console.log(event);
      this.selectedView = event.id;
    },
  },
};
</script>
