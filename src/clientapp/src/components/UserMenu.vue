<template>
  <v-menu min-width="200px" rounded>
    <template v-slot:activator="{ props }">
      <v-btn icon v-bind="props">
        <v-avatar
          size="small"
          :image="`https://api.dicebear.com/5.x/bottts-neutral/svg?seed=${user.name}`"
        >
        </v-avatar>
      </v-btn>
    </template>
    <v-card>
      <v-card-text>
        <div class="mx-auto d-flex flex-row align-center">
          <v-avatar
            class="mr-2"
            :image="`https://api.dicebear.com/5.x/bottts-neutral/svg?seed=${user.name}`"
            size="x-large"
          >
          </v-avatar>
          <div>
            <h3>{{ user.name }}</h3>
            <p class="text-caption mt-1">
              {{ user.email }}
            </p>
          </div>
        </div>
        <v-list density="compact">
          <v-divider></v-divider>
          <v-list-item class="pa-0">
            <v-btn
              variant="text"
              class="w-100"
              @click="openSubscriptionModal"
              >{{ tier }}</v-btn
            >
          </v-list-item>
          <v-divider></v-divider>
          <v-list-item class="pa-0">
            <v-btn variant="text" class="w-100" @click="accountManagement">
              Edit Account</v-btn
            >
          </v-list-item>
          <v-divider></v-divider>
          <v-list-item class="pa-0">
            <v-btn variant="text" class="w-100" @click="logout">Logout</v-btn>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>
  </v-menu>
  <SubscriptionModal :subscriptionModal="subscriptionModal" :tier="tier"/>
</template>
<script>
import SubscriptionModal from "@/components/SubscriptionModal.vue";
import {mapStores} from "pinia";
import {useUserStore} from "@/stores/userStore.js";

export default {
  name: "UserMenu",
  components: {SubscriptionModal},
  data() {
    return {
      user: {
        name: "",
        email: "",
      },
      tier:"",
      subscriptionModal:false,
    };
  },
  computed: {
    ...mapStores(useUserStore),
  },
  async mounted() {
    this.user = await this.$keycloak.loadUserInfo();
    this.userStore.setCurrentUser(this.user)
    this.getUserSubscription()
  },
  methods: {
    logout() {
      this.$keycloak.logout();
    },
    accountManagement() {
      this.$keycloak.accountManagement();
    },
    getUserSubscription() {
      if(this.userStore.user.tier){
        this.tier = this.userStore.user.tier
      }
      else
        this.tier = "Subscribe";
    },
    openSubscriptionModal(){
      this.subscriptionModal=true;
    }
  },
};
</script>
