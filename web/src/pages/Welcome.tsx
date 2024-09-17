import { PageContainer } from '@ant-design/pro-components';
import { Alert } from 'antd';
import React from 'react';

// MessagePreview 组件，增加 white-space 样式来处理换行符
const MessagePreview: React.FC<{ message: string; type?: 'success' | 'info' | 'warning' | 'error'; showIcon?: boolean; banner?: boolean }> = ({ message, type = 'info', showIcon = true, banner = false }) => {
  return (
    <Alert
      message={<span style={{ whiteSpace: 'pre-wrap' }}>{message}</span>}  // 使用 white-space: pre-wrap 处理换行
      type={type}             // 通知的类型
      showIcon={showIcon}     // 是否显示图标
      banner={banner}         // 是否以横幅形式展示
      style={{
        marginBottom: 24,     // 添加一些样式
      }}
    />
  );
};

const Welcome: React.FC = () => {
  return (
    <PageContainer>
      {/* 使用 \n 进行换行 */}
      <MessagePreview message={"欢迎来到 zouhr 的用户中心～\n" +
        "本项目基于React + SpringBoot开发完成，目前具有基本的登录、注册、管理员对用户的增删改查，重置密码等功能"} type="info" showIcon={false} banner={true} />
    </PageContainer>
  );
};

export default Welcome;
