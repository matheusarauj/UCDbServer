package psoft.backend.service;

import org.springframework.stereotype.Service;
import psoft.backend.dao.UserDAO;
import psoft.backend.exception.user.UserEmailInvalidoException;
import psoft.backend.exception.user.UserExistsException;
import psoft.backend.exception.user.UserInvalidoException;
import psoft.backend.exception.user.UserNullException;
import psoft.backend.model.TokenFilter;
import psoft.backend.model.User;

import javax.servlet.ServletException;
import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;
    private TokenFilter tokenFilter;

    UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.tokenFilter = new TokenFilter();
    }

    public User create(User user) throws UserExistsException {
        if (!user.validarEmail()) throw new UserEmailInvalidoException("Insira um e-mail válido");
        user.setEmail(user.getEmail().toLowerCase());
        User userVerify = findByEmail(user.getEmail());
        if (user.getPrimeiroNome() == null) throw new UserNullException("O primeiro nome não pode ser Null");
        if (user.getPrimeiroNome().trim().equals("")) throw new UserInvalidoException("O primeiro nome não pode ser vazio, insira um nome valido");
        if(user.getSenha().length() < 8 || user.getSenha().length() > 15) throw new UserInvalidoException("Deve inserir uma senha válida. Uma senha válida possui ente 8 e 15 caracteres");

        if (!(userVerify == null)) {
            throw new UserExistsException("Email já Cadastrado");
        }

        return userDAO.save(user);
    }

    public User findByEmail(String userLogin) {
        return (userDAO.findByEmail(userLogin));
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

    public void deleteAll() {
        userDAO.deleteAll();
    }

    public String getLogin(String auth) throws ServletException {
        return tokenFilter.getLogin(auth);
    }
}
