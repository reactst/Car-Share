import { defineStore } from "pinia";

export const useItemStore = defineStore("actions", {
  state: () => ({
    actions: [],
    activeActionIds: [],
  }),
  actions: {
    activateAction(actionId) {
      this.activeActionIds.push(actionId);
    },
    deactivateAction(actionId) {
      this.activeActionIds = this.activeActionIds.filter(
        (activeActionId) => activeActionId !== actionId
      );
    },
    clearActiveActions() {
      this.activeActionIds = [];
    },
    setActions(actions) {
      this.actions = actions;
    },
    clearActions() {
      this.actions = [];
    },
  },
});
