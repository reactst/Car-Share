<template>
  <v-dialog v-model="isOpen">
    <template v-slot:activator="{ props }">
      <v-btn v-bind="props" icon="mdi-face-agent"> </v-btn>
    </template>
    <v-card title="Support tickets">
      <v-data-table-server
        v-model:items-per-page="ticketStore.options.pageSize"
        :headers="ticketStore.tableHeaders"
        :items="ticketStore.ticketList"
        :items-length="ticketStore.options.itemsLength"
        :loading="ticketStore.loading"
        :sort-by="ticketStore.options.sortBy"
        @update:options="updateOptions($event)"
        class="h-100"
        height="85%"
      >
        <template v-slot:top>
          <v-toolbar
            flat
            height="80"
            class="d-flex align-center justify-center"
          >
            <div class="w-25 d-flex ml-5">
              <Datepicker
                :auto-apply="true"
                format="dd/MM/yyyy HH:mm"
                model-type="format"
                :enable-time-picker="false"
                :dark="theme === 'dark'"
                v-model="ticketStore.dateFrom"
                @update:model-value="loadTickets"
              ></Datepicker>
              <v-spacer></v-spacer>
              <Datepicker
                :auto-apply="true"
                format="dd/MM/yyyy HH:mm"
                model-type="format"
                :enable-time-picker="false"
                :dark="theme === 'dark'"
                v-model="ticketStore.dateTo"
                @update:model-value="loadTickets"
              ></Datepicker>
            </div>
          </v-toolbar>
        </template>
        <template v-slot:item.ticketTime="{ item }">
          {{ formatDate(item.raw.ticketTime) }}
        </template>
      </v-data-table-server>
    </v-card>
  </v-dialog>
</template>
<script>
import { useItemStore } from "@/stores/itemStore.js";
import { mapStores } from "pinia";
import { useUserStore } from "@/stores/userStore.js";
import { useTicketStore } from "@/stores/ticketStore.js";
import { format } from "date-fns";

export default {
  name: "SupportTicketTable",
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
