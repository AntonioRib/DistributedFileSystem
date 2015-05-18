Iniciar o sistema ---------------------------------------------

	1. Iniciar o servidor de contacto

	   Para iniciar o servidor de contacto basta execut�-lo sem quaisquer par�metros de entrada (este assumir� o nome de contactServer): 
		java server.contactServer.ContactServerClass

	   Se a inicializa��o ocorrer sem problemas ir� imprimir na consola uma mensagem de sucesso com o endere�o de onde est� localizado.

	2. Iniciar um servidor de ficheiros

	   Para iniciar um ou mais servidor de ficheiros executa-se os seguintes comandos, dependendo da implementa��o pretendida: 
		java server.fileServer.FileServerRMI serverName contactServerURL ou java server.fileServer.FileServerWS serverName contactServerURL

	   O par�metro contactServerURL pode ser omitido, permitindo que o servidor de ficheiros obtenha o endere�o do servidor de contacto por multicast.
	   Se a inicializa��o ocorrer sem problemas ir� imprimir na consola uma mensagem de sucesso com o endere�o de onde est� localizado.

	3. Iniciar um client

	   Para iniciar um client basta executar o comando: 
		java client.FileClient contactServerURL

	   Aqui, tamb�m o par�metro contactServerURL pode ser omitido, permitindo que o cliente obtenha o endere�o do servidor de contacto por multicast.
	   

O ponto 1 tem que ser executado previamente aos seguintes; no entanto, para o ponto 2 e o ponto 3 n�o existe a necessidade de uma ordem espec�fica.
O ficheiro policy.all dever� encontrar-se na directoria do projecto (ou na directoria bin se o programa estiver a ser executado fora do Eclipse).