package br.apolo.data.repository.impl;

import org.springframework.stereotype.Repository;

import br.apolo.data.model.User;
import br.apolo.data.repository.UserRepositoryCustom;

@Repository
public class UserRepositoryImpl extends BaseRepotitoryImpl<User> implements UserRepositoryCustom {

}