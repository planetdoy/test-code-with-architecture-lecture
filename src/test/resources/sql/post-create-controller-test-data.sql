
insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'doydoit@gmail.com', 'doydoit', 'Seoul', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ACTIVE', 0);

insert into `posts` (`content`, `created_at`, `modified_at`, `user_id`)
values ('helloworld', 1678530673958, 0, 1);