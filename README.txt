Iniciar o sistema ---------------------------------------------

	1. Iniciar o servidor de contacto

	   Para iniciar o servidor de contacto basta executá-lo sem quaisquer parâmetros de entrada (este assumirá o nome de contactServer): 
		java server.contactServer.ContactServerClass

	   Se a inicialização ocorrer sem problemas irá imprimir na consola uma mensagem de sucesso com o endereço de onde está localizado.

	2. Iniciar um servidor de ficheiros

	   Para iniciar um ou mais servidor de ficheiros executa-se os seguintes comandos, dependendo da implementação pretendida: 
		java server.fileServer.FileServerRMI serverName contactServerURL ou java server.fileServer.FileServerWS serverName contactServerURL

	   O parâmetro contactServerURL pode ser omitido, permitindo que o servidor de ficheiros obtenha o endereço do servidor de contacto por multicast.
	   Se a inicialização ocorrer sem problemas irá imprimir na consola uma mensagem de sucesso com o endereço de onde está localizado.

	3. Iniciar um client

	   Para iniciar um client basta executar o comando: 
		java client.FileClient contactServerURL

	   Aqui, também o parâmetro contactServerURL pode ser omitido, permitindo que o cliente obtenha o endereço do servidor de contacto por multicast.
	   

O ponto 1 tem que ser executado previamente aos seguintes; no entanto, para o ponto 2 e o ponto 3 não existe a necessidade de uma ordem específica.
O ficheiro policy.all deverá encontrar-se na directoria do projecto (ou na directoria bin se o programa estiver a ser executado fora do Eclipse).