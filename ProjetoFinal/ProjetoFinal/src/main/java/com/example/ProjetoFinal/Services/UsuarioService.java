package com.seuprojeto.service;

import com.seuprojeto.entity.Usuario;
import com.seuprojeto.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Lista todos os usuários cadastrados.
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca um usuário pelo ID. Lança exceção se não for encontrado.
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + id + " não encontrado."));
    }

    /**
     * Cria ou atualiza um usuário.
     */
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Deleta um usuário pelo ID, se existir.
     */
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário com ID " + id + " não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Busca usuário pelo e-mail, se houver essa coluna na entidade.
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Atualiza parcialmente dados de um usuário existente.
     */
    public Usuario atualizar(Long id, Usuario novosDados) {
        Usuario existente = buscarPorId(id);

        if (novosDados.getNome() != null) existente.setNome(novosDados.getNome());
        if (novosDados.getEmail() != null) existente.setEmail(novosDados.getEmail());
        if (novosDados.getSenha() != null) existente.setSenha(novosDados.getSenha());

        return usuarioRepository.save(existente);
    }
}
