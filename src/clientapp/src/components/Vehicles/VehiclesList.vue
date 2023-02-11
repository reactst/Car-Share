<template>
  <v-card title="Vehicles" variant="tonal">
    <v-data-table-server
      v-model:items-per-page="vehicleStore.options.pageSize"
      :headers="vehicleStore.headers"
      :items="vehicleStore.vehicleList"
      :loading="vehicleStore.loading"
      :sort-by="vehicleStore.options.sortBy"
      @update:options="updateOptions($event)"
      class="h-100"
    >
      <template v-slot:top>
        <v-toolbar flat height="80" class="d-flex align-center justify-center">
          <v-text-field
            class="mr-4 px-5"
            variant="outlined"
            append-inner-icon="mdi-magnify"
            v-model="vehicleStore.searchTerm"
            single-line
            label="Search"
            hide-details
            @click:append-inner="loadVehicles()"
          ></v-text-field>
          <v-toolbar-items>
            <v-btn variant="flat" color="purple" prepend-icon="mdi-plus" @click="addVehicle"
              >Add</v-btn
            >
          </v-toolbar-items>
        </v-toolbar>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5"
              >Are you sure you want to delete this item?</v-card-title
            >
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue-darken-1" variant="text" @click="closeDelete"
                >Cancel</v-btn
              >
              <v-btn
                color="blue-darken-1"
                variant="text"
                @click="deleteItemConfirm"
                >OK</v-btn
              >
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
      <template v-slot:item.actions="{ item }">
        <v-icon size="small" class="me-2" @click="openVehicleEdit(item.raw)">
          mdi-pencil
        </v-icon>
        <v-icon size="small" @click="deleteItem(item.raw)"> mdi-delete </v-icon>
      </template>
    </v-data-table-server>
  </v-card>
</template>
<script>
import { useItemStore } from "@/stores/itemStore.js";
import {useVehicleStore} from "@/stores/vehicleStore.js";
import { mapStores } from "pinia";

export default {
  name: "VehiclesList",
  data() {
    return {
      dialogAdd: false,
      dialogDelete: false,
      itemToDelete: {},
    };
  },
  async created() {
    await this.vehicleStore.loadVehicles(this.$http);
  },
  computed: {
    ...mapStores(useItemStore, useVehicleStore),
  },
  methods: {
    async openVehicleEdit(item) {
      this.currentItemStore.setLoading(true);
      this.currentItemStore.setCurrentItem(item);
    },

    deleteItem(item) {
      this.dialogDelete = true;
      this.itemToDelete = item;
    },
    async deleteItemConfirm() {
      this.$http.delete(`/api/vehicle/${this.itemToDelete.id}`);
      this.dialogDelete = false;
      await this.vehicleStore.loadVehicles(this.$http);
    },
    closeDelete () {
      this.dialogDelete = false;
    },
    loadVehicles(){
      this.vehicleStore.loadVehicles(this.$http)
    },
    updateOptions(options){
      this.vehicleStore.setOptions(options)
      this.vehicleStore.loadVehicles(this.$http)
    },
    addVehicle(){
      this.currentItemStore.setCurrentItem({
        id:null,
        make:"",
        model:"",
        modelYear:"",
        vin:"",
        registration:"",
        fuelType:"",
        subscriptionTier:"",
        status:"Created",
        location:[]
      })
    }
  },
};
</script>
