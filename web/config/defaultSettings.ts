import { Settings as LayoutSettings } from '@ant-design/pro-components';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'realDark',
  // 拂晓蓝
  primaryColor: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: 'zouhr的用户中心',
  pwa: false,
  logo: 'https://zhr-blog.oss-cn-beijing.aliyuncs.com/blog/202409081715636.png',
  iconfontUrl: '',
};

export default Settings;
