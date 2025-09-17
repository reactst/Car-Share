<template>
  <v-container class="h-100">
    <v-row class="h-100">
      <VehiclesList class="w-100 h-100" />
    </v-row>
    <VehicleEdit />
  </v-container>
</template>

<script>
import VehiclesList from "@/components/Vehicles/VehiclesList.vue";
import VehicleEdit from "@/components/Vehicles/VehicleEdit.vue";
export default {
  name: "VehiclesView",
  components: { VehiclesList, VehicleEdit },
};
</script>
