<template>
  <v-card title="Support tickets" variant="tonal">
    <v-data-table-server
      v-model:items-per-page="ticketStore.options.pageSize"
      :headers="ticketStore.headers"
      :items="ticketStore.ticketList"
      :items-length="ticketStore.options.itemsLength"
      :loading="ticketStore.loading"
      :sort-by="ticketStore.options.sortBy"
      @update:options="updateOptions($event)"
      class="h-100"
      height="85%"
    >
      <template v-slot:top>
        <v-toolbar flat height="80" class="d-flex align-center justify-center">
          <v-text-field
            class="mr-4 px-5"
            variant="outlined"
            append-inner-icon="mdi-magnify"
            v-model="ticketStore.searchTerm"
            single-line
            label="Search"
            hide-details
            @click:append-inner="loadTickets"
          ></v-text-field>
          <div class="w-25 d-flex ml-5">
            <Datepicker
                :show-now-button="true"
              :auto-apply="true"
              format="dd/MM/yyyy HH:mm"
              model-type="format"
              :enable-time-picker="false"
              :dark="theme === 'dark'"
              v-model="ticketStore.dateFrom"
              :max-date="new Date()"
              @update:model-value="loadTickets"
            ></Datepicker>
            <v-spacer></v-spacer>
            <Datepicker
                :show-now-button="true"
              :auto-apply="true"
              format="dd/MM/yyyy HH:mm"
              model-type="format"
              :enable-time-picker="false"
              :dark="theme === 'dark'"
              v-model="ticketStore.dateTo"
              :max-date="new Date()"
              @update:model-value="loadTickets"
            ></Datepicker>
          </div>
        </v-toolbar>
      </template>
      <template v-slot:item.ticketTime="{ item }">
        {{ formatDate(item.raw.ticketTime) }}
      </template>
      <template v-slot:item.actions="{ item }">
        <v-tooltip text="Open details">
          <template v-slot:activator="{ props }">
            <v-icon
              v-bind="props"
              size="small"
              class="me-2"
              @click="openTicketEdit(item.raw)"
            >
              mdi-file-document
            </v-icon>
          </template>
        </v-tooltip>
        <v-icon
          size="small"
          class="me-2"
          @click="openSupportTicketClose(item.raw)"
        >
          mdi-progress-close
        </v-icon>
        <v-dialog v-model="dialogCloseTicket" max-width="500px">
          <v-card>
            <v-card-title class="text-h5"
              >Are you sure you want to close this ticket ?</v-card-title
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue-darken-1"
                variant="tonal"
                @click="closeTicketConfirm"
                >Confirm</v-btn
              >
              <v-btn color="warning" variant="tonal" @click="cancelCloseTicket"
                >Cancel</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
    </v-data-table-server>
  </v-card>
</template>
<script>
import { useItemStore } from "@/stores/itemStore.js";
import { mapStores } from "pinia";

import { useUserStore } from "@/stores/userStore.js";
import { useTicketStore } from "@/stores/ticketStore.js";
import { format } from "date-fns";

export default {
  name: "SupportTicketList",
  props: {
    theme: {
      required: true,
    },
  },
  data() {
    return {
      isOpen: false,
      itemToClose: {},
      dialogCloseTicket: false,
    };
  },
  watch: {
    isOpen: async function () {
      if (this.isOpen)
        await this.ticketStore.loadTickets(this.$http, this.userStore.user.sub);
    },
  },
  computed: {
    ...mapStores(useItemStore, useTicketStore, useUserStore),
  },
  methods: {
    openTicketEdit(item) {
      this.currentItemStore.setLoading(true);
      this.currentItemStore.setCurrentItem(item);
    },
    formatDate(date) {
      return format(new Date(date), "dd/MM/yyyy HH:mm");
    },
    async loadTickets() {
      await this.ticketStore.loadTickets(this.$http, this.userStore.user.sub);
    },
    async updateOptions(options) {
      this.ticketStore.setOptions(options);
      await this.ticketStore.loadTickets(this.$http, this.userStore.user.sub);
    },
    openSupportTicketClose(item) {
      this.itemToClose = item;
      this.dialogCloseTicket = true;
    },
    closeTicketConfirm() {
      this.itemToClose.isClosed = true;
      this.$http.patch(`/api/supportticket/update/`, this.itemToClose);
      this.loadTickets();
    },
    cancelCloseTicket() {
      this.dialogCloseTicket = false;
    },
  },
};
</script>
