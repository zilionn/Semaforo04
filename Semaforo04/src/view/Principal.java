package view;

import java.util.concurrent.Semaphore;

import controller.ControllerEscuderia;

public class Principal {

	public static void main(String[] args) {
		Semaphore semaforo = new Semaphore(5);
		
		System.out.println(" == A corrida começou !! == ");
		
		for (int i = 1; i <= 7; i++) {
			Thread Escuderia = new ControllerEscuderia(i, semaforo);
			Escuderia.start();
		}
	}

}