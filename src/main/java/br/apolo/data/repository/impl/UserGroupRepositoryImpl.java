package br.apolo.data.repository.impl;

import org.springframework.stereotype.Repository;

import br.apolo.data.model.UserGroup;
import br.apolo.data.repository.UserGroupRepository;

@Repository(value = "userGroupRepository")
public class UserGroupRepositoryImpl extends JpaRepositoryImpl<UserGroup> implements UserGroupRepository {


}
