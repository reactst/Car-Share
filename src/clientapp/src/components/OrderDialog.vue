<template>
  <v-dialog v-model="currentItemSet">
    <v-container class="d-flex align-center justify-center">
      <v-card :height="500" :width="400">
        <v-row>
          <v-img
              :src="currentItemStore.currentItem.imageUrl"
              :aspect-ratio="16 / 9"
              cover
              :width="400"
          />
        </v-row>
        <v-row class="px-10 py-5">
          <v-text-field
              variant="outlined"
              label="Make"
              readonly
              :model-value="currentItemStore.currentItem.make"
          />
          <v-text-field
              variant="outlined"
              label="Model"
              readonly
              :model-value="currentItemStore.currentItem.model"
          />
          <v-text-field
              variant="outlined"
              label="Model year"
              readonly
              :model-value="currentItemStore.currentItem.modelYear"
          />
        </v-row>
        <v-row class="px-10">
          <v-text-field
              variant="outlined"
              label="Fuel"
              readonly
              :model-value="currentItemStore.currentItem.fuelType"
          />
          <v-text-field
              variant="outlined"
              label="Registration No."
              readonly
              :model-value="currentItemStore.currentItem.registration"
          />
          <v-text-field
              variant="outlined"
              label="Tier"
              readonly
              :model-value="currentItemStore.currentItem.subscriptionTier"
          />
        </v-row>
        <v-row class="px-10 justify-center">
          <v-btn variant="tonal" color="blue" @click="orderVehicle">Book now</v-btn>
        </v-row>
      </v-card>
    </v-container>
  </v-dialog>
</template>
<script>
export default {
  name: 'OrderDialog',
  props: {
    currentItemSet: {},
    orderVehicle: {}
  }
}
</script>