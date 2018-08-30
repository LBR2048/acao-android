package com.penseapp.acaocontabilidade.login.model

/**
 * Created by unity on 08/08/16.
 */
data class User (
    var key: String,
    var name: String = "name",
    var email: String = "email@email.com",
    var company: String = "company",
    var type: String = "type"
) {
    override fun toString(): String {
        return name
    }
}