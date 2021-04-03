--������� ����� ���� ����, �� ���� ����������
drop schema if exists education CASCADE;

--������ �����
create schema education;
--������������ ���������� 
commit;

--������ �������
create table education.teachers (id int not null primary key, name varchar(50), lastname varchar(50));
--��������� ���������� ����
alter table education.teachers add CONSTRAINT teachers_ukey unique (name, lastname);

--��������� 3 ������
insert into education.teachers (id, name, lastname)
values (1, '����', '����'), (2, '�����', '�����'), (3, '����', '����')
--���� ���� ������������� �������� id � �������, �� ���������� ���������� ��������
ON CONFLICT on CONSTRAINT teachers_pkey do update set name = excluded.name, lastname = excluded.lastname
;

--������ �������
create table education.lessons (
  id int not null primary key,
  subject varchar(50),
  teacher int,
  start timestamp,
  finish timestamp,
  --������� ����, �������� ��� id ������������� ���������� � ������� teachers
  FOREIGN KEY (teacher) REFERENCES education.teachers (id),
  --��������, ����� ������ ������ ������� ���������
  CHECK (start < finish)
  );
  --������ ����������� ������� �������
  alter table education.lessons add column duration interval GENERATED ALWAYS AS (finish - start) STORED;
--alter table education.lessons add CONSTRAINT FOREIGN KEY (teacher) REFERENCES education.teachers (id);

--���������� ��������, ������
insert into education.lessons (id, subject, teacher, start, finish)
values
(1, '����������', 1, '2020-11-14 09:01:00', '2020-11-14 10:30:00'),
(2, '����������', 1, '2020-11-14 12:00:00', '2020-11-14 13:30:00'),
(3, '����������', 1, '2020-11-15 09:00:00', '2020-11-15 10:30:00'),
(4, '����������', 1, '2020-11-15 11:30:00', '2020-11-15 13:00:00'),
(5, '����������', 1, '2020-11-15 15:30:00', '2020-11-15 17:00:00');
insert into education.lessons (id, subject, teacher, start, finish)
values
(6, '����������', 3, '2020-11-14 09:05:00', '2020-11-14 10:30:00'),
(7, '����������', 3, '2020-11-14 12:15:00', '2020-11-14 13:30:00'),
(8, '����������', 3, '2020-11-15 09:13:00', '2020-11-15 10:30:00'),
(9, '����������', 3, '2020-11-15 11:55:00', '2020-11-15 13:00:00'),
(10, '����������', 3, '2020-11-15 15:33:00', '2020-11-15 17:00:00');

--������ �������, � �������������� ��������� ����� join
select teachers.id, --id �������
       name,        --���
       lastname,    --�������
       duration,    --������������ ������
       avg_dur,     --������� ����������������� ������ � ����� � �����
       avg_dur_t,   --������� ����������������� ������ �� ��������
       extract(epoch from duration)/extract(epoch from avg_dur)*100 prcnt_of_avg_all, --������� ������������ ������ �� ����� ������� ������������
       extract(epoch from duration)/extract(epoch from avg_dur_t)*100 prcnt_of_avg_by_teacher--������� ������������ ������ �� ������� ������������ ��� �������
 from education.teachers --������� ���������
--�������������
inner join education.lessons --������� ������� ������
   on teachers.id = lessons.teacher --������������� �� ID �������
--������������� - ������� ��������� ������������
cross join (
  select avg(duration) as avg_dur --������� ����������������� ������ �� ����� �������
    from education.lessons --������� ������� ������
    ) t1 --��������� ������ � ����������� t1
--�������������
inner join (
  select teacher,--id �������
         avg(duration) as avg_dur_t --������� ����������������� ������ ��� �������
    from education.lessons --������� ������� ������
   group by teacher --���������� �� id �������, ��� ������� �������� ����� ���� ���������� ������
   ) t2 --��������� ������ � ����������� t2
  on teachers.id = t2.teacher --������������� �� ID �������
order by teachers.id, start; --��������� ������� �� �������, ����� �� ������ ������

--������ �������, ������ �����������, � �������� �������������� ���������
select teachers.id, --id �������
       name,        --���
       lastname,    --�������
       duration,    --������������ ������
       --������� ����������������� ������ � ����� � �����, ���� �� ����������, � ������ �� ��� ������ ��������� ������
       avg(duration) over () avg_all,
       --������� ����������������� ������ ��� �������, ���� id �������, ��� ������ � ���������� id �������
       avg(duration) over (partition by teachers.id) as avg_teach,
       --������� ������������ ������ �� ����� ������� ������������
       extract(epoch from duration)/extract(epoch from avg(duration) over ())*100 prcnt_of_avg_all,
       --������� ������������ ������ �� ������� ������������ ��� �������
       extract(epoch from duration)/extract(epoch from avg(duration) over (partition by teachers.id))*100 prcnt_of_avg_by_teacher,
       start,       --������ ������
       lag(start) over (partition by teachers.id order by start) prev_lession_start, --������ ���������� ������ � ������� (lag - ���������� ������)
       lead(start) over (partition by teachers.id order by start) next_lession_start --������ ��������� ������ � ������� (lead -��������� ������)
 from education.teachers --������� ���������
--�������������
inner join education.lessons --������� ������� ������
   on teachers.id = lessons.teacher --������������� �� ID �������
order by teachers.id, start; --��������� ������� �� �������, ����� �� ������ ������

--sql cte - Common Table Expressions
with t /*��������� t ��� ���������� �������*/ as (
select 3000 as value, '2020-01-01'::timestamp as start, '2020-02-01'::timestamp as finish --������ � 3 ���������� 
union  --�����������
select 5000, '2020-01-05'::timestamp, '2020-01-15'::timestamp --������ � 3 ���������� 
union  --�����������
select 7000, '2020-01-17'::timestamp, '2020-01-30'::timestamp --������ � 3 ���������� 
), --�������-������, �����, ����� ����� � ����, ����� ������
q /*��������� q ��� ���������� �������*/ as (
select start as date, value --���� ����� ����� � ����, � �����
from t --�� ������� � ����������� t
union
select finish as date, -value as value --���� ����� ������ ����, � ������������� �����
from t --�� ������� � ����������� t
),--�������, ���������� ������ ������ � ���� ������ "��������" �� ����� � ����������� ���� � �� ���������
res /*��������� res ��� ���������� �������*/ as (
select date, --���� �������� �������
       value, --����� ������� ��� �������
       sum(value) over (order by date) --������ (����� �������/������� ����������� ������)
from q --�� ������� � ����������� q
order by date --��������� �� ����
)--�������, ���������� ������ ������ � ���� ������ "��������" �� ����� � ����������� ���� � �� ��������� � �������� �� ������ ��������

--������ ������� �������� ������� � ������������ ����� ������ �� ���� ������
select * from
  (select date dt_from, --���� ��������� �������
          lead(date) over (order by date) dt_to, --���� ���������� ��������� �������
          sum  --������
   from res) r --���������
where sum = (select max(sum) from res /*������ � ������������ ��������� �������, ������������ ����*/)
;

--������� �������
create table education.authors (id serial primary key, name varchar(60), lastname varchar(60), patronym varchar(60));
--��������� �������
insert into education.authors (name, lastname, patronym) values
('���','�������','����������'),
('Ը���','�����������','����������'),
('������','�������',''),
('����','��������','���������');

--������� ����
create table education.books (author int, caption varchar(600), FOREIGN KEY (author) REFERENCES education.authors (id));
--��������� �����
insert into education.books (author, caption) values
((select id from education.authors where lastname like '�������'), '����� � ���'),
((select id from education.authors where lastname like '�������'), '���� ��������'),
((select id from education.authors where lastname like '�������'), '����� ����'),
((select id from education.authors where lastname like '�����������'), '������ ����������'),
((select id from education.authors where lastname like '�����������'), '�����'),
((select id from education.authors where lastname like '�����������'), '����'),
((select id from education.authors where lastname like '�������'), '�� ���'),
((select id from education.authors where lastname like '�������'), '������� ��������'),
((select id from education.authors where lastname like '�������'), '���� �������'),
((select id from education.authors where lastname like '��������'), '���� � ����');

--������ ���� ������ � id = 1
select * from education.books
left join education.authors on books.author = authors.id
where id = 1;

--�������� ������� �� ������ �������
create table education.t1 as
--������ ���� ������ � id = 2
select * from education.books
full join education.authors on books.author = authors.id
where id = 2;

--������ ������ � �������� � ����� � ����������, ����������� ��� ��������� ����, procedure SQL
DO $$
<<sql_anon_block>>
DECLARE
  --��������� ������ �� �������
  q cursor for select caption from education.books;
  --��������� ��������� ����������
  cap varchar(60);
BEGIN --�������� ����������
  open q; --��������� ������
  loop --�������� ���� �� �������
    fetch q into cap;    --���������� �������� �� ������� � ����������
    exit when not found; --������� �� �����, ���� ������ ������ �� ������
    insert into education.t1 (caption) values (cap); --��������� �������� ���������� � �������
  end loop; --��������� ���� �� �������
  close q; --��������� ������
END sql_anon_block $$;
