DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
    `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `roles` (`id`, `name`) VALUES
    ('4608c715-5d43-4cb8-9d2a-6f8783e5b004','Developer'),
    ('4b69c19a-c6a9-4189-8185-0da3ddcd7b96','Tester'),
    ('7cab013f-fe4f-4c19-8aea-b9c56c8f7661','Product Owner');

DROP TABLE IF EXISTS `memberships`;
CREATE TABLE `memberships` (
    `id` varchar(100) NOT NULL,
    `team` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `member` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `role` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '4608c715-5d43-4cb8-9d2a-6f8783e5b004',
    PRIMARY KEY (`id`),
    KEY `memberships_FK` (`role`),
    CONSTRAINT `memberships_FK` FOREIGN KEY (`role`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

