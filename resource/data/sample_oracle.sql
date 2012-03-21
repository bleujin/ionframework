create or replace PROCEDURE DROP_TABLE(TabName in Varchar2)
IS
    temp number:=0;
    tes VARCHAR2 (200) := TabName;
    drp_stmt VARCHAR2 (200):=null;
BEGIN
    select count(*) into temp from user_tables where TABLE_NAME = tes ;
    if temp =1 then
    drp_stmt := 'Drop Table '||tes;
    EXECUTE IMMEDIATE drp_stmt;
    end if;
    EXCEPTION
    WHEN OTHERS THEN
    raise_application_error(-20001,'An error was encountered - '||SQLCODE||' -ERROR- '||SQLERRM);
END DROP_TABLE;
/

call DROP_TABLE ('EMP_SAMPLE') ;
/

call DROP_TABLE ('DEPT_SAMPLE') ;
/

call DROP_TABLE ('BONUS_SAMPLE') ;
/

call DROP_TABLE ('SALGRADE_SAMPLE') ;
/

call DROP_TABLE ('COPY_SAMPLE') ;
/

CREATE TABLE EMP_SAMPLE
       (EMPNO INT NOT NULL,
        ENAME VARCHAR2(10),
        JOB VARCHAR2(9),
        MGR INT,
        HIREDATE DATE,
        SAL INT,
        COMM INT,
        DEPTNO INT);

INSERT INTO EMP_SAMPLE VALUES(7369, 'SMITH',  'CLERK',     7902, 	TO_DATE('17-12-1980', 'DD-MM-YYYY'),  800, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7499, 'ALLEN',  'SALESMAN',  7698,	TO_DATE('20-2-1981', 'DD-MM-YYYY'), 1600,  300, 30);
INSERT INTO EMP_SAMPLE VALUES(7521, 'WARD',   'SALESMAN',  7698,	TO_DATE('22-2-1981', 'DD-MM-YYYY'), 1250,  500, 30);
INSERT INTO EMP_SAMPLE VALUES(7566, 'JONES',  'MANAGER',   7839,	TO_DATE('2-4-1981', 'DD-MM-YYYY'),  2975, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7654, 'MARTIN', 'SALESMAN',  7698,	TO_DATE('28-9-1981', 'DD-MM-YYYY'), 1250, 1400, 30);
INSERT INTO EMP_SAMPLE VALUES(7698, 'BLAKE',  'MANAGER',   7839,	TO_DATE('1-5-1981', 'DD-MM-YYYY'),  2850, NULL, 30);
INSERT INTO EMP_SAMPLE VALUES(7782, 'CLARK',  'MANAGER',   7839,	TO_DATE('9-6-1981', 'DD-MM-YYYY'),  2450, NULL, 10);
INSERT INTO EMP_SAMPLE VALUES(7788, 'SCOTT',  'ANALYST',   7566,	TO_DATE('09-12-1982', 'DD-MM-YYYY'), 3000, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7839, 'KING',   'PRESIDENT', NULL,	TO_DATE('17-11-1981', 'DD-MM-YYYY'), 5000, NULL, 10);
INSERT INTO EMP_SAMPLE VALUES(7844, 'TURNER', 'SALESMAN',  7698,	TO_DATE('8-9-1981', 'DD-MM-YYYY'),  1500,    0, 30);
INSERT INTO EMP_SAMPLE VALUES(7876, 'ADAMS',  'CLERK',     7788,	TO_DATE('12-1-1983', 'DD-MM-YYYY'), 1100, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7900, 'JAMES',  'CLERK',     7698,	TO_DATE('3-12-1981', 'DD-MM-YYYY'),   950, NULL, 30);
INSERT INTO EMP_SAMPLE VALUES(7902, 'FORD',   'ANALYST',   7566,	TO_DATE('3-12-1981', 'DD-MM-YYYY'),  3000, NULL, 20);
INSERT INTO EMP_SAMPLE VALUES(7934, 'MILLER', 'CLERK',     7782,	TO_DATE('23-1-1982', 'DD-MM-YYYY'), 1300, NULL, 10);


CREATE UNIQUE INDEX EMP_SAMPLE_PK ON EMP_SAMPLE(EMPNO) ;


-- dept
CREATE TABLE DEPT_SAMPLE
       (DEPTNO INT,
        DNAME VARCHAR2(14),
        LOC VARCHAR2(13) );

INSERT INTO DEPT_SAMPLE VALUES (10, 'ACCOUNTING', 'NEW YORK');
INSERT INTO DEPT_SAMPLE VALUES (20, 'RESEARCH',   'DALLAS');
INSERT INTO DEPT_SAMPLE VALUES (30, 'SALES',      'CHICAGO');
INSERT INTO DEPT_SAMPLE VALUES (40, 'OPERATIONS', 'BOSTON');

CREATE UNIQUE INDEX DEPT_SAMPLE_PK ON DEPT_SAMPLE(DEPTNO) ;


-- bonus
CREATE TABLE BONUS_SAMPLE
        (ENAME VARCHAR2(10),
         JOB   VARCHAR2(9),
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
		(NO1 INT, NO2 VARCHAR2(2)) ; 

INSERT INTO COPY_SAMPLE
SELECT rownum no1, LPAD(rownum, 2, '0') NO2
FROM EMP_SAMPLE a, EMP_SAMPLE b
where rownum <= 31 ;



-- 
call DROP_TABLE ('UPDATE_SAMPLE') ;
/

create table UPDATE_SAMPLE (a int, b varchar2(10)) ;


call DROP_TABLE ('PERFORMANCE_SAMPLE') ;
/
create table PERFORMANCE_SAMPLE(a int, b char(150), c varchar2(50)) ;


call DROP_TABLE ('LOB_SAMPLE') ;
/
create table LOB_SAMPLE(a int, b varchar2(4000), c clob, d blob, e varchar2(200)) ;


commit ;


CREATE OR REPLACE PACKAGE Types
as type cursorType
is ref cursor ;
    FUNCTION dummy return number ;
END ;
/

CREATE OR REPLACE PACKAGE BODY Types
as 

FUNCTION dummy return Number
is
BEGIN
        return 1 ;
End dummy ;
END ;
/

CREATE OR REPLACE PACKAGE Sample
is
    function selectBy(v_a number) return Types.cursorType ;
    function insertWith(v_a number, v_b varchar2) return number ;
    
    function selectEmpBy return Types.cursorType ;
    function insertLobWith(v_a number, v_b CLOB) return number ;
    function selectLobBy(v_a number) return Types.cursorType ;
    
END Sample;
/


CREATE OR REPLACE PACKAGE BODY Sample
is

    function selectBy (v_a number)
    return Types.cursorType
    is
        rtn_cursor Types.cursorType ;
    begin
        open rtn_cursor For
        select * from update_sample where a = v_a ;
        
        return rtn_cursor ;
    end ;
    
    function insertWith(v_a number, v_b varchar2)
    return number
    is
    begin
        insert into update_sample values(v_a, v_b) ;
        return SQL%ROWCOUNT ;
    end ;
    

    function selectEmpBy 
    return Types.cursorType
    is
        rtn_cursor Types.cursorType ;
    begin
        open rtn_cursor For
        select * from emp_sample order by empno ;
        
        return rtn_cursor ;
    end ;


    function insertLobWith(v_a number, v_b CLOB) 
    return number 
    is
    begin
        insert into lob_sample(a, c) values(v_a, v_b) ;
        
        return SQL%ROWCOUNT ;
    end ;
    
    
    function selectLobBy(v_a number) 
    return Types.cursorType 
    is
        rtn_cursor Types.cursorType ;
    begin
        open rtn_cursor For
        select * from lob_sample where a = v_a ;
        
        return rtn_cursor ;
    end ;
    

End Sample ;
/
