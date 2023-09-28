package controller;

import java.util.concurrent.Semaphore;

public class ControllerEscuderia extends Thread{
	
	private static final int NUM_CARROS = 14;
	private static final int NUM_CARROS_ESCUDERIA = 2;
	
	private Semaphore semaforo;
	private int idEscuderia;
	
	private static double[][] tempoCarros = new double[NUM_CARROS][NUM_CARROS_ESCUDERIA];
	private static int carrosEmEspera = NUM_CARROS;
	
	public ControllerEscuderia(int idEscuderia, Semaphore semaforo) {
		this.semaforo = semaforo;
		this.idEscuderia = idEscuderia;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < NUM_CARROS_ESCUDERIA; i++) {
			int carroIndex = ((idEscuderia - 1) * NUM_CARROS_ESCUDERIA) + i;
			int carroNum = carroIndex + 1;
			
			try {
				semaforo.acquire();
				entrarPista(carroIndex, carroNum);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				sairPista(carroNum);
				semaforo.release();
			}
			
		}
	}

	private void entrarPista(int carroIndex, int carroNum) {
		System.out.println("O carro #" + carroNum + " (Escuderia " + idEscuderia + ") entrou na pista!");
		double voltaMaisRapida = 0;
		for (int j = 1; j <= 3; j++) {
			double tempoVolta = ((Math.random() * 20001) + 30000) / 1000; // 30 - 50 s.
			try {
				sleep((int) tempoVolta * 100); // Utiliza-se um valor menor (3 - 5 s.) para evitar que o programa demore muito em sua execução.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("O carro #" + carroNum + " (Escuderia " + idEscuderia + ") terminou sua " + j + "ª volta em " + String.format("%,.2f", tempoVolta) + " s.");
			voltaMaisRapida = (j == 1 || tempoVolta < voltaMaisRapida) ? tempoVolta : voltaMaisRapida;
		}
		tempoCarros[carroIndex][0] = voltaMaisRapida;
		tempoCarros[carroIndex][1] = carroNum;
	}

	
	
	private void sairPista(int carroNum) {
		System.out.println("O carro #" + carroNum + " (Escuderia " + idEscuderia + ") saiu da pista!");
		
		carrosEmEspera--;
		if (carrosEmEspera == 0) { //Este é o ultimo carro a terminar a corrida.
			mostrarResultados();
		}
	}

	private void mostrarResultados() {
		System.out.println(" == A corrida acabou!! == ");
		System.out.println("Resultados da corrida: ");
		
		double[] temp = new double[2];
		for (int i = 0; i < NUM_CARROS; i++) {
			for (int j = 0; j < (NUM_CARROS - 1) - i; j++) {
				if (tempoCarros[j][0] > tempoCarros[j + 1][0]) {
					temp = tempoCarros[j];
					tempoCarros[j] = tempoCarros[j + 1];
					tempoCarros[j + 1] = temp;
				}
			}
		}
		for (int i = 0; i < NUM_CARROS; i++) {
			System.out.println((i + 1) + "º ) Carro #" + (int) tempoCarros[i][1] + " = " + String.format("%,.2f", tempoCarros[i][0])  + " s.");
		}
	}
}