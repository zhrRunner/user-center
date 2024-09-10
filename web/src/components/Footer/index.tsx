import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
const Footer: React.FC = () => {
  const defaultMessage = 'zouhr的技术小屋';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'zouhr blog',
          title: (
            <>
              <a href="https://zhr.wiki" target="_blank" rel="noopener noreferrer">
                <img
                  src="https://zhr-blog.oss-cn-beijing.aliyuncs.com/blog/202311121409996.jpg"
                  alt="icon"
                  style={{ width: '20px', marginRight: '5px', borderRadius: '50%' }}
                />
                zhr&apos;s blog
              </a>
            </>
          ),
          href: 'https://zhr.wiki',
          blankTarget: true,
        },
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> GitHub项目地址
            </>
          ),
          href: 'https://github.com/zhrRunner/user-center',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
