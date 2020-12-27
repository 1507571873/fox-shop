<template>
  <div>
    <el-form>
      <el-form-item style="text-align: center">
        <el-button size="medium" @click="handlePrev">上一步，填写商品属性</el-button>
        <el-button type="primary" size="medium" @click="handleFinishCommit">完成，提交商品</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "ProductRelationDetail",
  props: {
    value: Object,
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {

    }
  },
  methods: {
    handlePrev(){
      this.$emit('prevStep');
    },
    handleFinishCommit(){
      this.$emit('finishCommit');
    }
  },
  mounted() {
  }
}
</script>

<style scoped>

</style>