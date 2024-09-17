import { useRef } from 'react';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { searchUsers, deleteById, resetPassword } from "@/services/ant-design-pro/api"; // 引入删除和重置密码的API
import { Image, message, Popconfirm } from "antd"; // 引入 Popconfirm 和 message 进行用户确认

const columns: ProColumns<API.CurrentUser>[] = [
  {
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
  },
  {
    title: '用户名',
    dataIndex: 'username',
    copyable: true,
  },
  {
    title: '用户账户',
    dataIndex: 'userAccount',
    copyable: true,
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    search: false,
    width: 120,
    render: (_, record) => (
      <div>
        <Image
          src={record.avatarUrl}
          width={50}
          height={50}
          style={{ borderRadius: '50%', objectFit: 'cover', height: '50px' }}
        />
      </div>
    ),
  },
  {
    title: '性别',
    dataIndex: 'gender',
    valueEnum: {
      0: { text: '女' },
      1: { text: '男' },
    },
  },
  {
    title: '电话',
    dataIndex: 'phone',
    copyable: true,
  },
  {
    title: '邮件',
    dataIndex: 'email',
    copyable: true,
  },
  {
    title: '状态',
    dataIndex: 'userStatus',
    valueEnum: {
      0: { text: '正常' },
      1: { text: '被封禁' },
    },
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    valueType: 'select',
    valueEnum: {
      0: { text: '普通用户', status: 'Default' },
      1: {
        text: '管理员',
        status: 'Success',
      },
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    search: false,
    valueType: 'dateTime',
  },
  {
    title: '操作',
    valueType: 'option',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={() => {
          action?.startEditable?.(record.id);
        }}
      >
        编辑
      </a>,
      <a href={record.avatarUrl} target="_blank" rel="noopener noreferrer" key="view">
        查看
      </a>,
      <Popconfirm
        key="delete"
        title="确定要删除这个用户吗？"
        onConfirm={async () => {
          const res = await deleteById({ id: record.id });
          if (res) {
            message.success('删除成功');
            action?.reload(); // 删除成功后重新加载表格
          } else {
            message.error('删除失败');
          }
        }}
        okText="确认"
        cancelText="取消"
      >
        <a>删除</a>
      </Popconfirm>,
      <Popconfirm
        key="resetPassword"
        title="确定要重置该用户的密码吗？"
        onConfirm={async () => {
          const res = await resetPassword({ id: record.id }); // 调用 resetPassword API
          if (res) {
            message.success('密码重置成功');
          } else {
            message.error('密码重置失败');
          }
        }}
        okText="确认"
        cancelText="取消"
      >
        <a>重置密码</a>
      </Popconfirm>,
    ],
  },
];

export default () => {
  const actionRef = useRef<ActionType>();
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      // @ts-ignore
      request={async (params = {}, sort, filter) => {
        const userList = await searchUsers();
        return {
          data: userList,
        }
      }}
      editable={{
        type: 'multiple',
      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      form={{
        syncToUrl: (values, type) => {
          if (type === 'get') {
            return {
              ...values,
              created_at: [values.startTime, values.endTime],
            };
          }
          return values;
        },
      }}
      pagination={{
        pageSize: 5,
      }}
      dateFormatter="string"
      headerTitle="用户信息"
    />
  );
};
