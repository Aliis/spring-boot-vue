// For authoring Nightwatch tests, see
// http://nightwatchjs.org/guide#usage

module.exports = {
  'default e2e tests': browser => {
    browser
        .url(process.env.VUE_DEV_SERVER_URL)
        .waitForElementVisible('#app', 5000)
        .assert.elementPresent('.login')
        .assert.elementCount('input', 2)
        .assert.elementCount('button', 4)
        .assert.elementCount('img', 2)
        .end()
  }
}
