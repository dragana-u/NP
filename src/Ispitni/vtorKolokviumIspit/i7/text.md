Да се (до)имплементира апликацијата за евиденција на работниот ангажман на вработени во една ИТ компанија. Препорака: За да немате проблем со исти имиња на класи користете посебни пакети за двете задачи, доколку сакате да ги решавате одделно.

За таа цел да се имплементира класата PayrollSystem во која што ќе се чуваат информации за вработени во компанијата. Како и во претходната задача постојат два типа на вработени HourlyEmployee и FreelanceEmployee. Пресметката на плата за двата типа на вработени е иста како и во претходната задача. За класата PayrollSystem да се имплементираат:

PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel) - ист како и во претходната задача
Employee createEmployee (String line) - метод којшто врз основа на влезен стринг во којшто се запишани информациите за даден вработен, ќе креира и врати објект од класа Employee. Дополнително, методот ќе го смести вработениот во системот за плати. Информациите за вработениот се во истиот формат како и во првата задача, со тоа што во овој случај има можност за бонус на вработениот којшто е одделен со празно место од информациите за вработениот. Постојат два типа на бонуси којшто вработениот може да ги добие и тоа:
Фиксен паричен бонус (запишан како бројка). пр. H;ID;level;hours; 100 (во овој случај добива фиксен бонус на плата од 100\$)
Процентуален паричен бонус (запишан како број со знак процент). пр. F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN; 10% (во овој случај добива процентуален бонус од 10% од неговата плата).
Во претходниот метод, со искчучок од тип BonusNotAllowedException да се спречи креирање на вработен на којшто му е доделен фиксен бонус поголем од 1000\$ или процентуален бонус поголем од 20%.
Map<String, Double> getOvertimeSalaryForLevels () - метод којшто ќе врати мапа каде што клучот е нивото на вработениот, а вредноста е вкупниот износ која што компанијата го исплатила за прекувремена работа за вработените од тоа ниво.
void printStatisticsForOvertimeSalary () - метод којшто ќе испечати статистки (минимум, максимум, сума, просек) на исплатените додатоци за прекувремена работа на сите вработени во компанијата.
Map<String, Integer> ticketsDoneByLevel() - метод којшто ќе врати мапа каде што клучот е нивото на вработените, а вредноста е бројот на поени за тикети што се сработени од вработените од соодветното ниво.
Collection<Employee> getFirstNEmployeesByBonus (int n) - метод којшто ќе врати сортирана колекција од првите n вработени сортирани во опаѓачки редослед според бонусот којшто го добиле на платата.
*Без два тест примери