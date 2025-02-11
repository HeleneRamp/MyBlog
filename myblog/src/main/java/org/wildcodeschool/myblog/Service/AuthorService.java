package org.wildcodeschool.myblog.Service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    //DTO for get all Authors
    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().map(authorMapper::convertToDTO).collect(Collectors.toList());
    }

    //DTO for get author by id
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            return null;
        }
        return authorMapper.convertToDTO(author);
    }

    //DTO for create an author
    public AuthorDTO createAuthor(Author author) {
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(savedAuthor);
    }

    //DTO for update an author
    public AuthorDTO updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            return null;
        }
        author.setFirstname(authorDetails.getFirstname());
        author.setLastname(authorDetails.getLastname());
        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(updatedAuthor);
    }

    //DTO for delete an author
    public boolean deleteAuthor(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            return false;
        } else {
            authorRepository.delete(author);
            return true;
        }
    }
}
