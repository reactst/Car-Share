import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    user: {
      address: "",
    },
  }),
  actions: {
    setCurrentUser(user) {
      this.user = user;
    },
    setAddress(address) {
      this.user.address = address;
    },
    clearCurrentUser() {
      this.user = {};
    },
  },
});
