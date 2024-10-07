SELECT 
    spid, 
    loginame 
FROM 
    sys.sysprocesses 
WHERE 
    loginame = 'alex';
	
KILL 64;

DROP LOGIN alex;
