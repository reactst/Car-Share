import { defineStore } from "pinia";
import { format } from "date-fns";
export const useTicketStore = defineStore("ticket", {
  state: () => ({
    ticketList: [],
    dateFrom: null,
    dateTo: format(new Date(), "dd/MM/yyyy HH:mm"),
    options: {
      page: 1,
      itemsPerPage: 10,
      sortBy: [{ key: "endTime", order: "desc" }],
      pageCount: 0,
    },
    searchTerm: "",
    loading: false,
    tableHeaders: [
      {
        title: "Title",
        key: "title",
      },
      { title: "Status", key: "status" },
      { title: "Created", key: "ticketTime" },
      { title: "Trip", key: "tripId", sortable: false },
    ],
    headers: [
      {
        title: "Title",
        key: "title",
      },
      { title: "Status", key: "status" },
      { title: "Created", key: "ticketTime" },
      { title: "User", key: "userId" },
      { title: "Trip", key: "tripId", sortable: false },
      { title: "Actions", key: "actions", sortable: false },
    ],
  }),
  actions: {
    setTicketList(list) {
      this.ticketList = list;
    },
    setOptions(options) {
      console.log(options);
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
        sortBy: [{ key: "endTime", order: "desc" }],
      };
    },
    setLoading(loading) {
      this.loading = loading;
    },
    clearTicketList() {
      this.ticketList = [];
    },
    async loadTickets(http, userId) {
      this.loading = true;
      let response = await http.get(
        `/api/supportticket?page=${this.options.page - 1}&size=${
          this.options.itemsPerPage
        }&dateFrom=${this.dateFrom?this.dateFrom : format(new Date(this.dateFrom), "dd/MM/yyyy HH:mm")}&dateTo=${this.dateTo ? this.dateTo : format(new Date(this.dateTo), "dd/MM/yyyy HH:mm")}&sort=${
          this.options.sortBy[0].key
        },${this.options.sortBy[0].order}&userId=${userId}&searchTerm=${this.searchTerm}`
      );
      this.ticketList = response.data.content;
      this.options.itemsLength = response.data.totalElements;
      this.loading = false;
    },
  },
});
