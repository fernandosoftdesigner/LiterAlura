package com.example.bookcatalog.service;

import com.example.bookcatalog.model.Author;
import com.example.bookcatalog.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> listAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> listLivingAuthors(int year) {
        return authorRepository.findAll().stream()
                .filter(author -> author.isAlive() && author.getBirthYear() <= year)
                .collect(Collectors.toList());
    }
}
