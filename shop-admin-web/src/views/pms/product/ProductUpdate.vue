<template>
  <product-detail :is-edit="true"></product-detail>
</template>

<script>
import ProductDetail from "./components/ProductDetail";
export default {
  name: "ProductAdd",
  components:{ProductDetail},
  data() {
    return {}
  },
  methods: {},
  mounted() {
  }
}
</script>

<style scoped>

</style>