ALTER TABLE bkv.user_role DROP CONSTRAINT bkv_user_role_user_fkey;
ALTER TABLE bkv.user_role ADD CONSTRAINT bkv_user_role_user_fkey FOREIGN KEY (user_id)
  REFERENCES bkv.user (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE;
  
ALTER TABLE bkv.user_role DROP CONSTRAINT bkv_user_role_role_fkey;
ALTER TABLE bkv.user_role ADD CONSTRAINT bkv_user_role_role_fkey FOREIGN KEY (role_id)
  REFERENCES bkv.dict_role (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE bkv.user_instance DROP CONSTRAINT bkv_user_instance_user_fkey;
ALTER TABLE bkv.user_instance ADD CONSTRAINT bkv_user_instance_user_fkey FOREIGN KEY (user_id)
  REFERENCES bkv.user (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE bkv.user_instance DROP CONSTRAINT bkv_user_instance_instance_fkey;
ALTER TABLE bkv.user_instance ADD CONSTRAINT bkv_user_instance_instance_fkey FOREIGN KEY (instance_id)
  REFERENCES bkv.dict_role (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE CASCADE;