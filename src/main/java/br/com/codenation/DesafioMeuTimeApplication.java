package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import br.com.codenation.Model.Jogador;
import br.com.codenation.Model.Time;
import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

	private static final String TIME_NAO_ENCONTRADO = "Time não encontrado.";
	private static final String JOGADOR_NAO_ENCONTRADO = "Jogador não encontrado.";

	private List<Time> times = new ArrayList<>();
	private List<Jogador> jogadores = new ArrayList<>();

	@Desafio("incluirTime")
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
		if (buscaTimePorId(id).isPresent()) {
			throw new IdentificadorUtilizadoException("Id de time em uso.");
		}

		times.add(new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario));
	}

	@Desafio("incluirJogador")
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
		if (buscaJogadorPorId(id).isPresent()) {
			throw new IdentificadorUtilizadoException("Id de jogador em uso.");
		}

		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
			throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
		}

		Jogador jogadorNovo = new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario);

		jogadores.add(jogadorNovo);
		time.get().adicionaJogador(jogadorNovo);
	}

	@Desafio("definirCapitao")
	public void definirCapitao(Long idJogador) {
		Optional<Jogador> jogador = buscaJogadorPorId(idJogador);

		if (!jogador.isPresent()) {
			throw new JogadorNaoEncontradoException("Id de jogador inválida.");
		}

		Optional<Time> time = buscaTimePorId(jogador.get().getIdTime());
		time.ifPresent(time1 -> time1.setCapitao(jogador.get()));
	}

	@Desafio("buscarCapitaoDoTime")
	public Long buscarCapitaoDoTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
			throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
		}

		if (time.get().getCapitao() == null) {
			throw new CapitaoNaoInformadoException("Capitão não informado para o time.");
		}

		return time.get().getCapitao().getId();
	}

	@Desafio("buscarNomeJogador")
	public String buscarNomeJogador(Long idJogador) {
		Optional<Jogador> jogador = buscaJogadorPorId(idJogador);

		if (jogador.isPresent()) {
			return jogador.get().getNome();
		}

		throw new JogadorNaoEncontradoException(JOGADOR_NAO_ENCONTRADO);
	}

	@Desafio("buscarNomeTime")
	public String buscarNomeTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (time.isPresent()) {
			return time.get().getNome();
		}

		throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
	}

	@Desafio("buscarJogadoresDoTime")
	public List<Long> buscarJogadoresDoTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
			throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
		}

		List<Jogador> jogadoresTime = time.get().getJogadores();

		return jogadoresTime.stream().map(Jogador::getId).sorted().collect(Collectors.toList());
	}

	@Desafio("buscarMelhorJogadorDoTime")
	public Long buscarMelhorJogadorDoTime(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
		    throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
        }

		List<Jogador> jogadoresTime = time.get().getJogadores();

		jogadoresTime.sort(
				Comparator.comparingInt(Jogador::getNivelHabilidade).reversed()
				.thenComparingLong(Jogador::getId)
		);

		return jogadoresTime.get(0).getId();
	}

	@Desafio("buscarJogadorMaisVelho")
	public Long buscarJogadorMaisVelho(Long idTime) {
		Optional<Time> time = buscaTimePorId(idTime);

		if (!time.isPresent()) {
			throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
		}

		List<Jogador> jogadoresTime = time.get().getJogadores();

		jogadoresTime.sort(
				Comparator.comparing(Jogador::getDataNascimento).reversed()
				.thenComparingLong(Jogador::getId)
		);

		return jogadoresTime.get(0).getId();
	}

	@Desafio("buscarTimes")
	public List<Long> buscarTimes() {
	    return this.times.stream().map(Time::getId).sorted().collect(Collectors.toList());
	}

	@Desafio("buscarJogadorMaiorSalario")
	public Long buscarJogadorMaiorSalario(Long idTime) {
        Optional<Time> time = buscaTimePorId(idTime);

        if (!time.isPresent()) {
            throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
        }

        List<Jogador> jogadoresTime = time.get().getJogadores();

        jogadoresTime.sort(
                Comparator.comparing(Jogador::getSalario).reversed()
                .thenComparingLong(Jogador::getId)
        );

        return jogadoresTime.get(0).getId();
	}

	@Desafio("buscarSalarioDoJogador")
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		Optional<Jogador> jogador = buscaJogadorPorId(idJogador);

		if (!jogador.isPresent()) {
		    throw new JogadorNaoEncontradoException(JOGADOR_NAO_ENCONTRADO);
        }

		return jogador.get().getSalario();
	}

	@Desafio("buscarTopJogadores")
	public List<Long> buscarTopJogadores(Integer top) {
        if (this.jogadores.size() == 0) {
            return new ArrayList<>();
        }

		this.jogadores.sort(Comparator.comparingInt(
		        Jogador::getNivelHabilidade).reversed()
                .thenComparingLong(Jogador::getId)
        );

		return this.jogadores.stream().map(Jogador::getId).collect(Collectors.toList()).subList(0, top);

	}

	@Desafio("buscarCorCamisaTimeDeFora")
	public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {
		Optional<Time> casa = buscaTimePorId(timeDaCasa);
		Optional<Time> fora = buscaTimePorId(timeDeFora);

		if (!casa.isPresent() || !fora.isPresent()) {
		    throw new TimeNaoEncontradoException(TIME_NAO_ENCONTRADO);
        }

		return casa.get().getCorUniformePrincipal().equals(fora.get().getCorUniformePrincipal()) ?
                fora.get().getCorUniformeSecundario() : fora.get().getCorUniformePrincipal();
	}

	private Optional<Time> buscaTimePorId(Long id) {
		return this.times.stream().filter(t -> t.getId().equals(id)).findFirst();
	}

	private Optional<Jogador> buscaJogadorPorId(Long id) {
		return this.jogadores.stream().filter(j -> j.getId().equals(id)).findFirst();
	}

}
