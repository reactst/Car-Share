<template>
  <v-app :theme="theme">
    <v-app-bar>
      <v-app-bar-title>BRK CarShare</v-app-bar-title>
      <v-btn variant="tonal" color="red" @click="endTrip" v-if="tripActive"
        >End Trip</v-btn
      >
      <template v-slot:append>
        <TripHistory :theme="theme"></TripHistory>
        <SupportTicketList :theme="theme"></SupportTicketList>
        <UserMenu />
        <v-btn
          :ripple="false"
          variant="plain"
          @click="onClick"
          :icon="theme === 'light' ? 'mdi-weather-sunny' : 'mdi-weather-night'"
        ></v-btn>
      </template>
    </v-app-bar>

    <v-main class="d-flex">
      <v-container fluid class="d-flex flex-column align-center">
        <div id="map" class="w-100 h-100 d-flex align-center">
          <v-card
            elevation="10"
            class="rounded ml-16 hidden-sm-and-down"
            width="450px"
            :height="600"
            style="z-index: 10"
            v-if="!tripActive"
          >
            <v-card-title class="my-8 text-h4">Catch a ride</v-card-title>
            <v-text-field
              label="Your location"
              variant="outlined"
              :readonly="true"
              class="mt-4 mx-4"
              :model-value="address"
              append-inner-icon="mdi-map-marker-account"
            ></v-text-field>
            <v-list class="overflow-x-scroll" :height="410">
              <v-list-item
                v-for="vehicle in vehiclesList"
                :key="vehicle.vin"
                class="v-card--hover"
                @click="openOrderModal(vehicle)"
              >
                <v-row>
                  <v-col>
                    <v-img
                      :src="vehicle.imageUrl"
                      :aspect-ratio="16 / 9"
                      cover
                      :width="200"
                    />
                  </v-col>
                  <v-col>
                    <div class="text-subtitle-2">
                      {{ vehicle.subscriptionTier }}
                    </div>
                    <div class="text-body-1">
                      {{ vehicle.make + " " + vehicle.model }}
                    </div>
                    <div class="mt-2">{{ vehicle.address }}</div>
                  </v-col>
                </v-row>
              </v-list-item>
            </v-list>
          </v-card>
        </div>
        <v-card class="flat w-100 h-75 hidden-md-and-up" v-if="!tripActive">
          <v-card-title class="my-5 text-h4">Catch a ride</v-card-title>

          <v-carousel :show-arrows="false" hide-delimiters>
            <v-carousel-item
              :v-if="vehiclesList.length !== 0"
              v-for="(vehicle, index) in vehiclesList"
              :key="vehicle"
              :value="index"
            >
              <v-card
                class="elevation-4 ml-4"
                :width="350"
                @click="openOrderModal(vehicle)"
              >
                <v-img :src="vehicle.imageUrl" :aspect-ratio="16 / 9" cover />
                <div>{{ vehicle.make + " " + vehicle.model }}</div>
                <div class="text-subtitle-2">
                  {{ vehicle.subscriptionTier }}
                </div>
              </v-card>
            </v-carousel-item>
          </v-carousel>
        </v-card>
      </v-container>
    </v-main>
    <v-dialog v-model="currentItemSet">
      <v-card
        :height="600"
        :width="450"
        class="align-self-center overflow-hidden"
      >
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
          <v-btn
            variant="tonal"
            color="blue"
            @click="orderVehicle(currentItemStore.currentItem)"
            >Book now</v-btn
          >
        </v-row>
      </v-card>
    </v-dialog>
    <v-dialog v-model="onDirections" persistent>
      <v-container class="d-flex align-center justify-center">
        <v-card
          :height="500"
          :width="400"
          class="d-flex flex-column align-center justify-space-evenly rounded-lg"
        >
          <div class="text-h6 px-5 mt-5">Please navigate to:</div>
          <div class="text-h5 px-5">
            {{ currentItemStore.currentItem.address }}
          </div>
          <v-icon icon="mdi-cellphone-nfc" class="mt-5" :size="250"></v-icon>
        </v-card>
      </v-container>
    </v-dialog>
    <v-snackbar v-model="failSnack" color="amber" variant="elevated"
      >Sorry, something went wrong on our end. Please try again.</v-snackbar
    >
  </v-app>
</template>

<script>
import { ref } from "vue";
import mapboxgl from "mapbox-gl/dist/mapbox-gl.js";
import constants from "@/constants.js";
import UserMenu from "@/components/UserMenu.vue";
import { useItemStore } from "@/stores/itemStore.js";
import { useUserStore } from "@/stores/userStore.js";
import { mapStores } from "pinia";
import TripHistory from "@/components/Trips/TripHistory.vue";
import SupportTicketList from "@/components/SupportTickets/SupportTicketTable.vue";
import { useVehicleStore } from "@/stores/vehicleStore.js";
// import MapboxDirections from "@mapbox/mapbox-gl-directions";

export default {
  components: { SupportTicketList, UserMenu, TripHistory },
  data() {
    return {
      theme: ref("dark"),
      address: null,
      vehiclesList: [],
      currentItemSet: false,
      vehicleKey: null,
      failSnack: false,
      failSnackMsg: "Sorry, something went wrong on our end. Please try again.",
      onDirections: false,
      map: null,
      location: null,
      tripActive: false,
      directions: null,
    };
  },
  computed: {
    ...mapStores(useItemStore, useUserStore, useVehicleStore),
  },
  async mounted() {
    navigator.geolocation.watchPosition(this.getCurrentPosition);
    // this.directions = new MapboxDirections({
    //   accessToken: constants.accessToken,
    //   unit: 'metric',
    //   profile:'mapbox/driving'
    // });
    mapboxgl.accessToken = constants.accessToken;
    this.map = new mapboxgl.Map({
      container: "map",
      style: constants.mapStyle,
      center: [16.4435, 43.5147],
      zoom: 13,
    });
    this.map.addControl(new mapboxgl.NavigationControl());
    this.map.addControl(
      new mapboxgl.GeolocateControl({
        positionOptions: {
          enableHighAccuracy: true,
        },
        trackUserLocation: true,
        showUserHeading: true,
      })
    );

    await this.vehicleStore.loadVehicles(this.$http);
    this.vehiclesList = this.vehicleStore.vehicleList;

    this.map.on("load", () => {
      this.map.loadImage(
        "https://docs.mapbox.com/mapbox-gl-js/assets/custom_marker.png",
        (error, image) => {
          if (error) throw error;
          this.map.addImage("custom-marker", image);
        }
      );
      let pointFeatures = [];
      this.vehiclesList.forEach((vehicle) => {
        pointFeatures.push({
          type: "Feature",
          geometry: {
            type: "Point",
            coordinates: [vehicle.location[0], vehicle.location[1]],
          },
          properties: {
            title: vehicle.make + " " + vehicle.model,
          },
        });
      });

      this.map.addSource("points", {
        type: "geojson",
        data: {
          type: "FeatureCollection",
          features: pointFeatures,
        },
      });

      this.map.addLayer({
        id: "points",
        type: "symbol",
        source: "points",
        layout: {
          "icon-image": "custom-marker",
          // get the title name from the source's "title" property
          "text-field": ["get", "title"],
          "text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
          "text-offset": [0, 1.25],
          "text-anchor": "top",
        },
      });
    });

    this.getCurrentPosition();
    this.vehiclesList.map(async (vehicle) => {
      vehicle.address = await this.getAddressFromLocation(vehicle.location);
    });
  },
  methods: {
    getCurrentPosition: function () {
      navigator.geolocation.getCurrentPosition(async (location) => {
        this.location = [location.coords.longitude, location.coords.latitude];
        this.address = await this.getAddressFromLocation(this.location);
      });
    },
    onClick() {
      this.theme = this.theme === "light" ? "dark" : "light";
    },
    openOrderModal(item) {
      this.currentItemStore.setCurrentItem(item);
      this.currentItemSet = true;
    },
    async orderVehicle(vehicle) {
      vehicle.status = "Booked";
      this.currentItemSet = false;

      this.vehicleKey = await this.$http.get("/api/key/generate");
      this.onDirections = true;
      try {
        // const ndef = new NDEFReader();
        // await ndef.write(this.vehicleKey);
        let resp = await this.$http.patch("api/vehicle/update", vehicle);
        if (resp.status === 200) {
          this.currentItemStore.setCurrentItem(vehicle);
          delete vehicle.address;
          resp = await this.$http.post(
            `api/trip/start?userId=${this.userStore.user.sub}`,
            vehicle
          );
          this.onDirections = false;
          this.currentItemStore.setCurrentTrip(resp.data);
          this.tripActive = true;
          // this.map.addControl(this.directions, "top-left");
        } else {
          this.failSnackMsg =
            "There was an issue booking your vehicle, please try again.";
          this.failSnack = true;
        }
      } catch (error) {
        console.error("NDEF error: " + error);
        this.onDirections = false;
        this.failSnackMsg = "Your device doesn't support this action";
        this.failSnack = true;
      }
    },
    async getAddressFromLocation(location) {
      let response = await this.$http.get(
        `https://api.mapbox.com/geocoding/v5/mapbox.places/${location[0]},${location[1]}.json?access_token=${constants.accessToken}&country=HR`
      );
      return response.data.features[0].place_name;
    },
    async endTrip() {
      // this.map.removeControl(this.directions);
      navigator.geolocation.getCurrentPosition(async (location) => {
        this.location = [location.coords.longitude, location.coords.latitude];
      });
      await this.$http.post(
        `api/trip/end?location=${this.location}`,
        this.currentItemStore.currentTrip
      );
      let vehicle = this.currentItemStore.currentItem;
      vehicle.status = "Available";
      await this.$http.patch(`api/vehicle/update`, vehicle);
      this.onDirections = false;
      this.currentItemStore.clearCurrentTrip();
      this.tripActive = false;
      this.vehiclesList.map(async (vehicle) => {
        vehicle.address = await this.getAddressFromLocation(vehicle.location);
      });
    },
  },
};
</script>
