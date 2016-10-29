ALTER TABLE `seguridad`.`user`
ADD UNIQUE INDEX `username_UNIQUE` (`username` ASC),
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC);
