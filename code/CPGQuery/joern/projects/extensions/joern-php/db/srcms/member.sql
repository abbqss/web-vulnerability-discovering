
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `pid` varchar(255) NOT NULL DEFAULT '0' COMMENT '个人资料ID',
  `username` varchar(20) DEFAULT '路人甲' COMMENT '用户昵称',
  `realname` varchar(100) NOT NULL DEFAULT '暂无' COMMENT '真实姓名',
  `team` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '团队名称',
  `email` varchar(100) DEFAULT '暂无' COMMENT '用户邮箱',
  `salt` varchar(9) NOT NULL DEFAULT '暂无' COMMENT '加密salt',
  `password` varchar(32) DEFAULT NULL COMMENT '用户密码',
  `token` varchar(255) NOT NULL DEFAULT '0' COMMENT '防护token',
  `avatar` varchar(255) DEFAULT '暂无' COMMENT '用户头像',
  `address` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '用户住址',
  `description` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '个人简介',
  `bankcode` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '银行账号',
  `idcode` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '身份证号',
  `zipcode` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '邮编',
  `alipay` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '支付宝账号',
  `tel` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '联系电话',
  `website` varchar(255) NOT NULL DEFAULT '暂无' COMMENT '个人网站',
  `qqnumber` varchar(255) NOT NULL DEFAULT '0' COMMENT 'QQ号',
  `create_at` varchar(11) DEFAULT '0' COMMENT '创建时间',
  `update_at` varchar(11) DEFAULT '0' COMMENT '更新时间',
  `login_ip` varchar(20) DEFAULT '0' COMMENT '登录IP',
  `status` tinyint(1) DEFAULT '1' COMMENT '0:禁止登陆 1:正常',
  `type` tinyint(1) DEFAULT '1' COMMENT '1:前台用户 2:管理员 ',
  `jifen` int(10) NOT NULL DEFAULT '0' COMMENT '用户积分',
  `jinbi` varchar(255) NOT NULL DEFAULT '0' COMMENT '安全币',
  PRIMARY KEY (`id`),
  KEY `username` (`username`) USING BTREE,
  KEY `password` (`password`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'0','user','暂无','暂无','user@qq.com','暂无','5cc32e366c87c4cb49e4309b75f57d64','0','暂无','暂无','暂无','暂无','暂无','暂无','暂无','暂无','暂无','0','1497262271','0','0.0.0.0',1,1,0,'0'),(2,'40490179412345254132823132685141','[已删除]','[已删除]','[已删除]','0','0','905ee8f75384669deca8b221fa28eda4','0','暂无','暂无','[已删除]','暂无','暂无','暂无','暂无','暂无','[已删除]','0','1497262735','1497262736','0.0.0.0',1,1,200,'200'),(3,'23655135121160235158753959640175','user2','暂无','暂无','user2@qq.com','ZvWtKuAr','a42001f146d8351d83bd50613708d0c6','6cd213daa5e168af1e3c19748824a3f5','暂无','暂无','暂无','暂无','暂无','暂无','暂无','暂无','暂无','0','1498998699','1504923888','0.0.0.0',1,1,100,'70');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

