package apolo.data.repository.impl;

import apolo.data.model.User;
import apolo.data.repository.UserRepositoryCustom;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends BaseRepotitoryImpl<User> implements UserRepositoryCustom {

}