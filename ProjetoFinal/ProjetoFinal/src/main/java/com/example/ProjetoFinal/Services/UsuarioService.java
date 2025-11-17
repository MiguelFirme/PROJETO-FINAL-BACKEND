package com.example.ProjetoFinal.Services;

import com.example.ProjetoFinal.Entidades.Usuario;
import com.example.ProjetoFinal.Repositorys.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário com ID " + id + " não encontrado."));
    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deletar(UUID id) {
        Usuario usuario = buscarPorId(id); // Carrega a entidade (e a carteira associada)
        usuarioRepository.delete(usuario); // Deleta a entidade carregada, acionando o cascade
    }
}
