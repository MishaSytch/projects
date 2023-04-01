--Сотрудника с максимальной заработной платой.
select NAME, max(SALARY) as SALARY
from EMPLOYEE;


--Отдел, с максимальной суммарной зарплатой сотрудников.
select DEPARTMENT_ID, SUM(SALARY) as Summary
from EMPLOYEE
join DEPARTMENT D on D.ID = EMPLOYEE.DEPARTMENT_ID
group by D.ID, D.NAME
order by Summary desc limit 1;

--Вывести одно число: максимальную длину цепочки руководителей по таблице сотрудников (вычислить глубину дерева).
select
    count(*) as Summary
from EMPLOYEE




-- Сотрудника, чье имя начинается на «Р» и заканчивается на «н».
select NAME
from EMPLOYEE
where NAME like 'Р%н';