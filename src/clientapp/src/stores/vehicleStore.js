import { defineStore } from "pinia";
import _ from "lodash";
export const useVehicleStore = defineStore("vehicle", {
  state: () => ({
    vehicleList: [],
    options: {
      page: 1,
      itemsPerPage: 10,
      sortBy: [{ key: "make", order: "desc" }],
      pageCount:0
    },
    searchTerm: "",
    loading: false,
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
      { title: "Actions", key: "actions", sortable: false },
    ],
  }),
  actions: {
    setVehicleList(list) {
      this.vehicleList = list;
    },
    setOptions(options) {
      this.options.page = options.page;
      this.options.itemsPerPage = options.itemsPerPage;
      if (options.sortBy.length !== 0) {
        this.options.sortBy[0].key = options.sortBy[0].key;
        this.options.sortBy[0].order = options.sortBy[0].order;
      }
    },
    clearOptions() {
      this.options = {
        page: 1,
        pageSize: 10,
        sortBy: [{ key: "Make", order: "desc" }],
      };
    },
    setLoading(loading) {
      this.loading = loading;
    },
    clearVehicleList() {
      this.vehicleList = [];
    },
    async loadVehicles(http) {
      this.loading = true;
      let response = await http.get(
        `/api/vehicle?page=${this.options.page - 1}&size=${
          this.options.pageSize
        }&searchTerm=${this.searchTerm}&sort=${this.options.sortBy[0].key},${
          this.options.sortBy[0].order
        }`
      );
      this.vehicleList = response.data.content;
      this.options.pageCount = response.data.totalPages;
      this.loading = false;
    },
  },
});
