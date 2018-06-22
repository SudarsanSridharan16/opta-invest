<template>
  <div class="container-fluid">
    <b-row>
      <b-col cols="12">
        <h1>{{ msg }}</h1>
      </b-col>
    </b-row>
    <b-row>
      <b-col cols="6">
        <b-alert variant="danger" :show="showDismissibleAlert" @dismissed="showDismissibleAlert=false" dismissible>
          {{ erroMsg }}
        </b-alert>
        <b-card-group deck>
          <b-card header-tag="header">
            <div slot="header">
                <b-link href="#" @click="load">Load Sample</b-link>
            </div>
            <b-form @submit="onSubmit" @reset="onReset">
              <b-form-group id="input-group">
                <b-form-textarea id="portfolio-content" v-model="text" :rows="12" :max-rows="12"/>
              </b-form-group>
              <b-button type="submit" variant="primary">Submit</b-button>
              <b-button type="reset" variant="danger">Reset</b-button>
            </b-form>
          </b-card>
        </b-card-group>
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from 'axios'
import SAMPLE from '@/sample/data'

export default {
  name: 'new-portfolio',
  data () {
    return {
      msg: 'New Portfolio',
      erroMsg: '',
      text: ''
    }
  },
  computed: {
    showDismissibleAlert: {
      get () {
        return this.erroMsg.length > 0
      },
      set (newVal) {
        if (!newVal) this.erroMsg = ''
      }
    }
  },
  methods: {
    load () {
      this.text = JSON.stringify(SAMPLE, null, 2)
    },
    onSubmit (evt) {
      evt.preventDefault()
      const vm = this
      try {
        axios.post('/api/portfolio', JSON.parse(this.text))
        .then(res => {
          vm.$router.push({ name: 'portfolios' })
        })
        .catch(error => {
          vm.erroMsg = error.message
        })
      } catch (e) {
        vm.erroMsg = e.message
      }
    },
    onReset (evt) {
      this.text = ''
    }
  }
}
</script>
