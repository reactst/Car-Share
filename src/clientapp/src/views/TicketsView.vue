<template>
  <v-container class="h-100">
    <v-row class="h-100">
      <SupportTicketList :theme="theme" class="w-100 h-100" />
    </v-row>
    <TicketEdit :theme="theme"/>
  </v-container>
</template>

<script>


import SupportTicketList from "@/components/SupportTickets/SupportTicketList.vue";
import TicketEdit from "@/components/Tickets/TicketEdit.vue";
export default {
  name: "TicketsView",
  props:{
    theme:{
      required:true
    }
  },
  components: {SupportTicketList, TicketEdit },
};
</script>
