
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
DROP TABLE IF EXISTS `hall`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hall` (
  `id` int(10) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '名人堂昵称',
  `team` varchar(255) NOT NULL DEFAULT 'Unknown' COMMENT '团队名称',
  `url` varchar(100) NOT NULL COMMENT '名人堂头像URL',
  `des` varchar(100) NOT NULL COMMENT '名人堂介绍',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='名人堂';
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40000 ALTER TABLE `hall` DISABLE KEYS */;
INSERT INTO `hall` VALUES (0,'Nancy Rich','Google (Porject Zero)','./PUBLIC/Index/img/400x400/04.jpg','来自Google Project Zero的Nancy，第一季度帮助我们发现20个涉及Andriod、Google Chrome等核心产品的严重漏洞。对Google安全生态的建设起到了极大的帮助 '),(1,'Anna Kusaikina','Apple Security Team','./Public/Index/img/400x400/06.jpg','来自Apple Security Team的Anna，第三季度帮助我们发现5个涉及Google Chrome的高危漏洞，对Chrome的稳定性和安全性的提升贡献非凡。'),(2,'Microsoft Security Center','Microsoft Security Response Center','./Public/Index/img/400x400/05.jpg','帮助我们发现了一枚严重级别的远程代码执行漏洞，并及时通知我们进行修复，保护了亿万用户的安全，特此表示衷心的感谢。');
/*!40000 ALTER TABLE `hall` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

