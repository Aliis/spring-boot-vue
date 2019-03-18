import { shallowMount } from '@vue/test-utils';
// import User from '@/components/User'

describe('Login.vue', () => {
    it('should render Login Button', () => {
        const wrapper = shallowMount(Login);
        const contentButton = wrapper.find('v-btn');
        expect(contentButton.text()).toEqual('Login');
    })
})
