IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[emp_sample]'))
DROP table [dbo].[emp_sample]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[dept_sample]'))
DROP table [dbo].[dept_sample]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[bonus_sample]'))
DROP table [dbo].[bonus_sample]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[salgrade_sample]'))
DROP table [dbo].[salgrade_sample]

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[copy_sample]'))
DROP table [dbo].[copy_sample]


CREATE TABLE EMP_SAMPLE
       (EMPNO INT NOT NULL,
        ENAME VARCHAR(10),
        JOB VARCHAR(9),
        MGR INT,
        HIREDATE DATETIME,
        SAL INT,
        COMM INT,
        DEPTNO INT);

INSERT INTO EMP_SAMPLE VALUES(7369, 'SMITH',  'CLERK',     7902, 	convert(DATETIME, '17-DEC-1980', 106),  800, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7499, 'ALLEN',  'SALESMAN',  7698,	convert(DATETIME, '20-FEB-1981', 106), 1600,  300, 30);
INSERT INTO EMP_SAMPLE VALUES(7521, 'WARD',   'SALESMAN',  7698,	convert(DATETIME, '22-FEB-1981', 106), 1250,  500, 30);
INSERT INTO EMP_SAMPLE VALUES(7566, 'JONES',  'MANAGER',   7839,	convert(DATETIME, '2-APR-1981', 106),  2975, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7654, 'MARTIN', 'SALESMAN',  7698,	convert(DATETIME, '28-SEP-1981', 106), 1250, 1400, 30);
INSERT INTO EMP_SAMPLE VALUES(7698, 'BLAKE',  'MANAGER',   7839,	convert(DATETIME, '1-MAY-1981', 106),  2850, NULL, 30);
INSERT INTO EMP_SAMPLE VALUES(7782, 'CLARK',  'MANAGER',   7839,	convert(DATETIME, '9-JUN-1981', 106),  2450, NULL, 10);
INSERT INTO EMP_SAMPLE VALUES(7788, 'SCOTT',  'ANALYST',   7566,	convert(DATETIME, '09-DEC-1982', 106), 3000, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7839, 'KING',   'PRESIDENT', NULL,	convert(DATETIME, '17-NOV-1981', 106), 5000, NULL, 10);
INSERT INTO EMP_SAMPLE VALUES(7844, 'TURNER', 'SALESMAN',  7698,	convert(DATETIME, '8-SEP-1981', 106),  1500,    0, 30);
INSERT INTO EMP_SAMPLE VALUES(7876, 'ADAMS',  'CLERK',     7788,	convert(DATETIME, '12-JAN-1983', 106), 1100, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7900, 'JAMES',  'CLERK',     7698,	convert(DATETIME, '3-DEC-1981', 106),   950, NULL, 30);
INSERT INTO EMP_SAMPLE VALUES(7902, 'FORD',   'ANALYST',   7566,	convert(DATETIME, '3-DEC-1981', 106),  3000, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7934, 'MILLER', 'CLERK',     7782,	convert(DATETIME, '23-JAN-1982', 106), 1300, NULL, 10);


CREATE UNIQUE INDEX EMP_SAMPLE_PK ON EMP_SAMPLE(EMPNO) ;


-- dept
CREATE TABLE DEPT_SAMPLE
       (DEPTNO INT,
        DNAME VARCHAR(14),
        LOC VARCHAR(13) );

INSERT INTO DEPT_SAMPLE VALUES (10, 'ACCOUNTING', 'NEW YORK');
INSERT INTO DEPT_SAMPLE VALUES (20, 'RESEARCH',   'DALLAS');
INSERT INTO DEPT_SAMPLE VALUES (30, 'SALES',      'CHICAGO');
INSERT INTO DEPT_SAMPLE VALUES (40, 'OPERATIONS', 'BOSTON');

CREATE UNIQUE INDEX DEPT_SAMPLE_PK ON DEPT_SAMPLE(DEPTNO) ;


-- bonus
CREATE TABLE BONUS_SAMPLE
        (ENAME VARCHAR(10),
         JOB   VARCHAR(9),
         SAL   INT,
         COMM  INT);


-- salgrade
CREATE TABLE SALGRADE_SAMPLE
        (GRADE INT,
         LOSAL INT,
         HISAL INT);

INSERT INTO SALGRADE_SAMPLE VALUES (1,  700, 1200);
INSERT INTO SALGRADE_SAMPLE VALUES (2, 1201, 1400);
INSERT INTO SALGRADE_SAMPLE VALUES (3, 1401, 2000);
INSERT INTO SALGRADE_SAMPLE VALUES (4, 2001, 3000);
INSERT INTO SALGRADE_SAMPLE VALUES (5, 3001, 9999);

CREATE UNIQUE INDEX SALGRADE_SAMPLE_PK ON SALGRADE_SAMPLE(GRADE) ;


CREATE TABLE COPY_SAMPLE
		(NO1 INT, NO2 VARCHAR(2)) ; 

INSERT INTO COPY_SAMPLE
SELECT ROWNUM no1, Replicate('0', 2-(Len(ROWNUM))) + convert(varchar(2), ROWNUM) no2
FROM (
	SELECT top 31 row_number() over(order by a.empNO) AS ROWNUM
	FROM EMP_SAMPLE a, EMP_SAMPLE b
	) base




-- 
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[update_sample]'))
DROP table [dbo].[update_sample]

create table update_sample (a int, b varchar(10)) ;


IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[performance_sample]'))
DROP table [dbo].[performance_sample]

create table performance_sample(a int, b char(150), c varchar(50)) ;


IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[lob_sample]'))
DROP table [dbo].[lob_sample]

create table lob_sample(a int, b varchar(8000), c text, d image, e varchar(200)) ;



GO


IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sample@insertWith]'))
DROP Procedure [dbo].[sample@insertWith] ;

GO 

CREATE Procedure sample@insertWith(@_a int, @_b varchar(20))
as
BEGIN
	INSERT INTO update_sample values(@_a, @_b);
	return @@Rowcount
END ;

GO 

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sample@selectBy]'))
DROP procedure [dbo].[sample@selectBy] ;

GO 

CREATE PROCEDURE sample@selectBy(@_a int)
as
BEGIN
	SELECT * FROM update_sample WHERE a = @_a ;
END ;


GO 

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sample@selectEmpBy]'))
DROP procedure [dbo].[sample@selectEmpBy] ;

GO 

CREATE PROCEDURE sample@selectEmpBy
as
BEGIN
	SELECT * FROM emp_sample order by empno;
END ;

GO

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sample@insertLobWith]'))
DROP procedure [dbo].[sample@insertLobWith] ;

GO 

CREATE procedure sample@insertLobWith(@_a int, @_b text)
as
BEGIN
	insert into lob_sample(a, c) values(@_a, @_b);
	return @@Rowcount
END ;


GO 

IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[sample@selectLobBy]'))
DROP procedure [dbo].[sample@selectLobBy] ;

GO 

CREATE PROCEDURE sample@selectLobBy(@_a int)
as
BEGIN
	SELECT * FROM lob_sample where a = @_a ;
END ;



commit ;