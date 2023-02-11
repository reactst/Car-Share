<template>
  <v-card title="Tickets" variant="tonal">
    <v-data-table-server
      v-model:items-per-page="itemsPerPage"
      :headers="headers"
      :items="ticketList"
      :loading="loading"
      :search="searchTerm"
      @update:options="loadTickets"
      @click:row="openTicketDetails"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-text-field
            class="mr-4 px-5"
            variant="outlined"
            append-inner-icon="mdi-magnify"
            v-model="searchTerm"
            single-line
            label="Search"
            @click:append-inner="loadTickets"
          ></v-text-field>
        </v-toolbar></template
      >
    </v-data-table-server>
  </v-card>
</template>
<script>
import { useItemStore } from "@/stores/itemStore.js";
import { mapStores } from "pinia";

export default {
  name: "TicketList",
  data() {
    return {
      ticketList: [],
      searchTerm: "",
      loading: false,
      page: 1,
      length: 0,
      itemsPerPage: 5,
      headers: [
        {
          title: "Make",
          key: "make",
        },
        { title: "Model", key: "model" },
        { title: "Year", key: "modelYear" },
        { title: "Vin", key: "vin" },
        { title: "Registration", key: "registration" },
        { title: "Fuel", key: "fuelType" },
        { title: "Tier", key: "subscriptionTier" },
        { title: "Status", key: "status" },
      ],
    };
  },
  async created() {
    this.loadTickets();
  },
  computed: {
    ...mapStores(useItemStore),
  },
  methods: {
    openTicketDetails(item) {
      this.currentItemStore.setLoading(true);
      this.currentItemStore.setCurrentItem(item);
    },
    async loadTickets(event) {
      this.loading = true;
      let response;
      if (event === undefined || event["type"] !== undefined) {
        response = await this.$http.get(
          `/api/supportticket?page=${this.page - 1}&size=10&searchTerm=${
            this.searchTerm
          }`
        );
      } else {
        console.log(event);
        response = await this.$http.get(
          `/api/supportticket?page=${event.page - 1}&size=${
            event.itemsPerPage
          }&searchTerm=${this.searchTerm}&sort=${
            event.sortBy.length !== 0 ? event.sortBy[0].key : ""
          },${event.sortBy.length !== 0 ? event.sortBy[0].order : ""}`
        );
      }
      this.ticketList = response.data.content;
      this.length = response.data.totalPages;
      this.loading = false;
    },
  },
};
</script>
