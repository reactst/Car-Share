<template>
  <v-dialog v-model="localsubscriptionModal">
    <v-container class="d-flex align-center justify-center">
      <v-card :height="800" :width="600" class="d-flex flex-column align-center justify-space-evenly rounded-lg"
              v-if="tier === 'Subscribe'">
        <div class="text-h3 px-5 mt-5">
          Subscription plans
        </div>
        <v-card class="w-75 mt-10 bg-green">
          <v-row class="pl-10 align-center">
            <v-col cols="5">
              <div>Basic | 39.99 EUR</div>
              <v-btn class="mt-4" variant="tonal" @click="subscribeUser('Basic')">
                Activate
              </v-btn>
            </v-col>
            <v-col>
              <v-list class="bg-transparent" density="compact">
                <v-list-item>Free parking</v-list-item>
                <v-list-item>Budget-friendly</v-list-item>
                <v-list-item>Enviromental</v-list-item>
              </v-list>
            </v-col>
          </v-row>
        </v-card>
        <v-card class="mt-2 w-75 bg-blue">
          <v-row class="pl-10 align-center">
            <v-col cols="5">
              <div>Standard | 69.99 EUR</div>
              <v-btn class="mt-4" variant="tonal" @click="subscribeUser('Standard')">
                Activate
              </v-btn>
            </v-col>
            <v-col>
              <v-list class="bg-transparent" density="compact">
                <v-list-item>Everything included in Basic</v-list-item>
                <v-list-item>Roadside assistance</v-list-item>
                <v-list-item>Life Insurance policy</v-list-item>
              </v-list>
            </v-col>
          </v-row>
        </v-card>
        <v-card class="mt-2 w-75 bg-red">
          <v-row class="pl-10 align-center">
            <v-col cols="5">
              <div>Premium | 199.99 EUR</div>
              <v-btn class="mt-4" variant="tonal" @click="subscribeUser('Standard')">
                Activate
              </v-btn>
            </v-col>
            <v-col>
              <v-list class="bg-transparent" density="compact">
                <v-list-item>Everything included in Standard</v-list-item>
                <v-list-item>Access to all vehicles</v-list-item>
                <v-list-item>24/7 support</v-list-item>
              </v-list>
            </v-col>
          </v-row>
        </v-card>
      </v-card>
      <v-card :height="500" :width="400" class="d-flex flex-column align-center justify-space-evenly rounded-lg"
              v-if="tier !== 'Subscribe'">
        <div class="text-h6 px-5 mt-5">
          Subscription tier:
        </div>
        <div class="text-h5 px-5">
          {{ tier }}
        </div>
      </v-card>
    </v-container>
  </v-dialog>
</template>
<script>
import { useUserStore } from "@/stores/userStore.js";
import { mapStores } from "pinia";

export default {
  name: 'SubscriptionModal',
  props: {
    subscriptionModal: {},
    tier: {}
  },
  computed: {
    ...mapStores(useUserStore),
  },
  data(){
    return{
      localsubscriptionModal:false,
    }
  },
  watch:{
    subscriptionModal:function (){
      this.localsubscriptionModal = this.subscriptionModal
    }
  },
  methods:{
    subscribeUser(tier){
      let subscribedUser = this.userStore.user
      subscribedUser.tier = tier
      this.userStore.setCurrentUser(subscribedUser)
      this.localsubscriptionModal=false
    }
  }
}
</script>