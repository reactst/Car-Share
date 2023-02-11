import { defineStore } from "pinia";

export const useItemStore = defineStore("currentItem",  {
  state:()=>({
    currentItem: {},
    currentTrip:{},
    loading:false
  }),
  actions:{
    setCurrentItem(item) {
      this.currentItem = item;
    },
    setLoading(loading){
      this.loading = loading;
    },
    clearCurrentItem(){
      this.currentItem = {}
    },
    setCurrentTrip(trip){
      this.currentTrip = trip
    },
    clearCurrentTrip(){
      this.currentItem={}
    }
  }
});
