<template>
  <div class="text-center d-flex align-center">
    <v-dialog v-model="currentItemSet" max-width="600px">
      <v-card>
        <v-card-title>
          {{"Suport ticket id: " + localTicket.id}}
        </v-card-title>
        <v-card-text>
          <v-col class="d-flex flex-row">
            <v-text-field
              class="w-50"
              v-model="localTicket.title"
              label="Title"
              readonly
            ></v-text-field>
            <v-checkbox
              v-model="localTicket.isClosed"
              label="Closed"
              readonly
            ></v-checkbox>
          </v-col>
          <v-col>
            <v-textarea
              v-model="localTicket.description"
              label="Description"
              readonly
            ></v-textarea>
          </v-col>
          <v-col>
            <Datepicker
              :dark="theme==='dark'"
              v-model="localTicket.ticketTime"
            ></Datepicker>
          </v-col>
          <v-col>
            <v-text-field
              v-model="localTicket.status"
              label="Status"
              readonly
            ></v-text-field>
          </v-col>
          <v-col> </v-col>
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="primary"
            @click="closeTicket"
            v-if="
              currentItemStore.currentItem.status !== 'Cleaning' &&
              currentItemStore.currentItem.status === 'Available'
            "
            >Close ticket</v-btn
          >
          <v-btn color="primary" @click="currentItemStore.clearCurrentItem()"
            >Close</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { useItemStore } from "@/stores/itemStore.js";
import { mapStores } from "pinia";
export default {
  props: {
    theme: {
      required: true,
    },
  },
  components: {},

  data() {
    return {
      localTicket: {},
    };
  },
  updated() {
    this.currentItemStore.setLoading(false);
  },
  computed: {
    ...mapStores(useItemStore),
    currentItemSet: function () {
      return (
        this.currentItemStore.currentItem.id !== undefined &&
        this.currentItemStore.currentItem.title
      );
    },
  },
  watch: {
    currentItemSet: function () {
      if (this.currentItemSet) {
        this.localTicket = { ...this.currentItemStore.currentItem };
      }
    },
  },
  methods: {
    isSingleLine(text) {
      return text.indexOf("\n") === -1;
    },
    async closeTicket() {
      let ticket = this.currentItemStore.currentItem;
      ticket.isClosed = true;
      this.currentItemStore.setCurrentItem(ticket);
      await this.$http.patch(`/api/supportticket/update`, ticket);
    },
  },
};
</script>
