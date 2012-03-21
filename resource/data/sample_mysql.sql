drop table if exists emp_sample ;
drop table if exists dept_sample ;
drop table if exists bonus_sample ;
drop table if exists salgrade_sample ;
drop table if exists copy_sample ;

CREATE TABLE EMP_SAMPLE
       (EMPNO INT NOT NULL,
        ENAME VARCHAR(10),
        JOB VARCHAR(9),
        MGR INT,
        HIREDATE DATE,
        SAL INT,
        COMM INT,
        DEPTNO INT);

INSERT INTO EMP_SAMPLE VALUES(7369, 'SMITH',  'CLERK',     7902, 	STR_TO_DATE('17-DEC-1980', '%d-%M-%Y'),  800, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7499, 'ALLEN',  'SALESMAN',  7698,	STR_TO_DATE('20-FEB-1981', '%d-%M-%Y'), 1600,  300, 30);
INSERT INTO EMP_SAMPLE VALUES(7521, 'WARD',   'SALESMAN',  7698,	STR_TO_DATE('22-FEB-1981', '%d-%M-%Y'), 1250,  500, 30);
INSERT INTO EMP_SAMPLE VALUES(7566, 'JONES',  'MANAGER',   7839,	STR_TO_DATE('2-APR-1981', '%d-%M-%Y'),  2975, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7654, 'MARTIN', 'SALESMAN',  7698,	STR_TO_DATE('28-SEP-1981', '%d-%M-%Y'), 1250, 1400, 30);
INSERT INTO EMP_SAMPLE VALUES(7698, 'BLAKE',  'MANAGER',   7839,	STR_TO_DATE('1-MAY-1981', '%d-%M-%Y'),  2850, NULL, 30);
INSERT INTO EMP_SAMPLE VALUES(7782, 'CLARK',  'MANAGER',   7839,	STR_TO_DATE('9-JUN-1981', '%d-%M-%Y'),  2450, NULL, 10);
INSERT INTO EMP_SAMPLE VALUES(7788, 'SCOTT',  'ANALYST',   7566,	STR_TO_DATE('09-DEC-1982', '%d-%M-%Y'), 3000, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7839, 'KING',   'PRESIDENT', NULL,	STR_TO_DATE('17-NOV-1981', '%d-%M-%Y'), 5000, NULL, 10);
INSERT INTO EMP_SAMPLE VALUES(7844, 'TURNER', 'SALESMAN',  7698,	STR_TO_DATE('8-SEP-1981', '%d-%M-%Y'),  1500,    0, 30);
INSERT INTO EMP_SAMPLE VALUES(7876, 'ADAMS',  'CLERK',     7788,	STR_TO_DATE('12-JAN-1983', '%d-%M-%Y'), 1100, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7900, 'JAMES',  'CLERK',     7698,	STR_TO_DATE('3-DEC-1981', '%d-%M-%Y'),   950, NULL, 30);
INSERT INTO EMP_SAMPLE VALUES(7902, 'FORD',   'ANALYST',   7566,	STR_TO_DATE('3-DEC-1981', '%d-%M-%Y'),  3000, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7934, 'MILLER', 'CLERK',     7782,	STR_TO_DATE('23-JAN-1982', '%d-%M-%Y'), 1300, NULL, 10);


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
SELECT @RNUM:=@RNUM+1 AS ROWNUM, LPAD(@RNUM:=@RNUM, 2, '0') NO2
FROM (SELECT @RNUM:=0) R, EMP_SAMPLE a, EMP_SAMPLE b
LIMIT 31 ;



-- 
create table if not exists update_sample (a int, b varchar(10)) ;


create table if not exists performance_sample(a int, b char(150), c varchar(50)) ;

create table if not exists lob_sample(a int, b text, c MEDIUMTEXT, d MEDIUMBLOB, e varchar(200)) ;

commit ;


-- 이건 수동으로 돌려야 =ㅅ=;;
delimiter //

drop function if exists sample_insertWith //
CREATE Function sample_insertWith(v_a int, v_b varchar(20))
returns int
BEGIN
	INSERT INTO update_sample values(v_a, v_b);
	return 1 ;
END//


drop procedure if exists sample_selectBy //
CREATE PROCEDURE sample_selectBy(v_a int)
BEGIN
	SELECT * FROM update_sample WHERE a = v_a ;
END//

drop procedure if exists sample_selectEmpBy //
CREATE PROCEDURE sample_selectEmpBy()
BEGIN
	SELECT * FROM emp_sample order by empno ;
END//

drop function if exists sample_insertLobWith //
CREATE Function sample_insertLobWith(v_a int, v_c text)
returns int
BEGIN
	INSERT INTO lob_sample(a, c) values(v_a, v_c);
	return 1 ;
END//

drop procedure if exists sample_selectLobBy //
CREATE PROCEDURE sample_selectLobBy(v_a int)
BEGIN
	SELECT * FROM lob_sample where a = v_a ;
END//

commit ;
