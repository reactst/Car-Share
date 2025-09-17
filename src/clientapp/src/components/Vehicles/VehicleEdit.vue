<template>
  <div class="text-center d-flex align-center">
    <v-dialog v-model="currentItemSet" class="w-50">
      <v-card>
        <v-card-text>
          <v-col>
            <v-text-field v-model="localVehicle.make" label="Make"></v-text-field>
            <v-text-field v-model="localVehicle.model" label="Model"></v-text-field>
          </v-col>
          <v-col>
            <v-text-field v-model="localVehicle.modelYear" label="Model year"></v-text-field>
            <v-text-field v-model="localVehicle.vin" label="VIN"></v-text-field>
          </v-col>
          <v-col>
            <v-text-field v-model="localVehicle.registration" label="Registration"></v-text-field>
            <v-text-field v-model="localVehicle.fuelType" label="Fuel type"></v-text-field>
          </v-col>
          <v-col>
            <v-select
              v-model="localVehicle.subscriptionTier"
              :items="['Premium', 'Standard', 'Basic']"
            placeholder="Subscription tier"></v-select>
          </v-col>
          <v-col>
            <v-text-field v-model="localVehicle.imageUrl" label="Image url"></v-text-field>
          </v-col>
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="primary"
            @click="orderCleaning"
            v-if="localVehicle.status === 'Available'"
            >Request cleaning</v-btn
          >
          <v-btn
            color="primary"
            @click="orderService"
            v-if="localVehicle.status === 'Available'"
            >Request service</v-btn
          >
          <v-btn
            color="primary"
            @click="retrieveVehicle"
            v-if="localVehicle.status !== 'Available' && localVehicle.status !== 'Created'"
            >Retrieve vehicle</v-btn
          >
          <v-btn color="green" variant="tonal" @click="saveEditVehicle" v-if="isEdited"
            >Save</v-btn
          >
          <v-btn color="red" variant="tonal" @click="cancel()" v-if="isEdited">Cancel</v-btn>
          <v-btn color="white" variant="tonal" @click="close()" v-if="!isEdited">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { useItemStore } from "@/stores/itemStore.js";
import {useVehicleStore} from "@/stores/vehicleStore.js";
import { mapStores } from "pinia";
import _ from "lodash";

export default {
  data() {
    return {
      edited: false,
      localVehicle: {},
    };
  },
  updated() {
    this.currentItemStore.setLoading(false);
  },
  computed: {
    ...mapStores(useItemStore, useVehicleStore),
    currentItemSet: function () {
      return (
        this.currentItemStore.currentItem.id !== undefined
      );
    },
    isEdited: function () {
      return !_.isEqual(this.currentItemStore.currentItem, this.localVehicle);
    },
  },
  watch: {
    currentItemSet: function () {
      if (this.currentItemSet) {
        this.localVehicle = { ...this.currentItemStore.currentItem };
      }
    },
  },
  methods: {
    isSingleLine(text) {
      return text.indexOf("\n") === -1;
    },
    async orderCleaning() {
      this.localVehicle.status = "Cleaning";
    },
    async orderService() {
      this.localVehicle.status = "In service";
    },
    async retrieveVehicle() {
      this.localVehicle.status = "Available";
    },
    async close(){
      this.currentItemStore.clearCurrentItem();
    },
    async cancel() {
      this.localVehicle = this.currentItemStore.currentItem;
    },
    async saveEditVehicle() {
      console.log(this.localVehicle)
      if(this.localVehicle.status === "Created"){
        this.localVehicle.status="Available"
        this.localVehicle.location[0]="16.4435"
        this.localVehicle.location[1]="43.5147"
        await this.$http.post(`/api/vehicle/add`, this.localVehicle)
        this.currentItemStore.clearCurrentItem();
        this.vehicleStore.clearOptions();
        await this.vehicleStore.loadVehicles(this.$http);
      }
      else{
        await this.$http.patch(`/api/vehicle/update`, this.localVehicle);
        this.currentItemStore.clearCurrentItem();
        this.vehicleStore.clearOptions();
        await this.vehicleStore.loadVehicles(this.$http);
      }
    },
  },
};
</script>
