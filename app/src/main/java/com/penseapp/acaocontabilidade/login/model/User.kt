package com.penseapp.acaocontabilidade.login.model

class User (
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