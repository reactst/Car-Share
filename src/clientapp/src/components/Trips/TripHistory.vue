<template>
  <v-dialog v-model="isOpen">
    <template v-slot:activator="{ props }">
      <v-btn v-bind="props" icon="mdi-clipboard-text"> </v-btn>
    </template>
    <v-card title="Trip history">
      <v-data-table-server
        v-model:items-per-page="tripStore.options.pageSize"
        :headers="tripStore.headers"
        :items="tripStore.tripList"
        :items-length="tripStore.options.itemsLength"
        :loading="tripStore.loading"
        :sort-by="tripStore.options.sortBy"
        @update:options="updateOptions($event)"
        class="h-100"
        height="85%"
      >
        <template v-slot:top>
          <v-toolbar flat height="80" class="d-flex align-center justify-center">
            <div class="w-25 d-flex ml-5">
              <Datepicker
                  :show-now-button="true"
                :auto-apply="true"
                format="dd/MM/yyyy HH:mm"
                model-type="format"
                :enable-time-picker="false"
                :dark="theme === 'dark'"
                v-model="tripStore.dateFrom"
                @update:model-value="loadTrips"
              ></Datepicker>
              <v-spacer></v-spacer>
              <Datepicker
                  :show-now-button="true"
                :auto-apply="true"
                format="dd/MM/yyyy HH:mm"
                model-type="format"
                :enable-time-picker="false"
                :dark="theme === 'dark'"
                v-model="tripStore.dateTo"
                @update:model-value="loadTrips"
              ></Datepicker>
            </div>
          </v-toolbar>
        </template>
        <template v-slot:item.startTime="{ item }">
          {{formatDate(item.raw.startTime)}}
        </template>
        <template v-slot:item.endTime="{ item }">
          {{formatDate(item.raw.endTime)}}
        </template>
        <template v-slot:item.actions="{ item }">
          <v-icon
            size="small"
            class="me-2"
            @click="openSupportTicketCreate(item.raw)"
          >
            mdi-face-agent
          </v-icon>
          <v-dialog v-model="dialogCreate" max-width="500px">
            <v-card>
              <v-card-title class="text-h5"
                >Support ticket for trip:
                {{ currentItemStore.currentItem.tripId }}</v-card-title
              >
              <v-row class="d-flex flex-column">
                <v-col class="mt-5">
                  <v-text-field
                    label="Title"
                    v-model="newTicket.title"
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-textarea
                    label="Description"
                    v-model="newTicket.description"
                  ></v-textarea>
                </v-col>
              </v-row>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="blue-darken-1"
                  variant="tonal"
                  @click="createTicketConfirm"
                  >Create</v-btn
                >
                <v-btn color="warning" variant="tonal" @click="closeCreate"
                  >Cancel</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </template>
      </v-data-table-server>
    </v-card>
  </v-dialog>
</template>
<script>
import { useItemStore } from "@/stores/itemStore.js";
import { mapStores } from "pinia";
import { useTripStore } from "@/stores/tripStore.js";
import { useUserStore } from "@/stores/userStore.js";
import {format} from "date-fns";

export default {
  name: "TripsHistory",
  props: {
    theme: {
      required: true,
    },
  },
  data() {
    return {
      isOpen: false,
      dialogCreate: false,
      itemToDelete: {},
      dateFrom: "",
      dateTo: "",
      newTicket: {
        id: null,
        title: "",
        description: "",
        isClosed: false,
        status: "Created",
        ticketTime: "",
        userId: "",
      },
    };
  },
  watch: {
    isOpen: async function () {
      if (this.isOpen)
        await this.tripStore.loadTrips(this.$http, this.userStore.user.sub);
    },
  },
  computed: {
    ...mapStores(useItemStore, useTripStore, useUserStore),
  },
  methods: {
    formatDate(date){
      return format(new Date(date), "dd/MM/yyyy HH:mm")
    },
    async openSupportTicketCreate(item) {
      console.log(item);
      this.dialogCreate = true;
      this.currentItemStore.setLoading(true);
      this.currentItemStore.setCurrentItem(item);
    },
    closeCreate() {
      this.dialogCreate = false;
    },
    clearTicket(){
      this.newTicket={
        title: "",
        description: "",
      }
    },
    async createTicketConfirm() {
      this.$http.post(`/api/supportticket/create?userId=${this.userStore.user.sub}&tripId=${this.currentItemStore.currentItem.tripId}&title=${this.newTicket.title}&description=${this.newTicket.description}`);
      this.dialogCreate = false;
      this.clearTicket()
      await this.tripStore.loadTrips(this.$http);
    },
    loadTrips() {
      this.tripStore.loadTrips(this.$http, this.userStore.user.sub);
    },
    updateOptions(options) {
      this.tripStore.setOptions(options);
      this.tripStore.loadTrips(this.$http, this.userStore.user.sub);
    },
  },
};
</script>
