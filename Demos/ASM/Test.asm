# Primes.asm

start:
  print_text "Prime Number Finder"
  newline
  print_text "The range of numbers between 2 and 100 contains the following primes:"
  newline

forEach_n_in_2_to_100:
  literal_a  2
  write_a    n
  literal_a  100
  write_a    high

while_n_le_high:
  read_a n
  read_b high
  cmp
  jgt    done

is_n_equal_to_2:
  literal_b 2
  cmp
  jeq       is_prime

is_n_multiple_of_2:
  mod
  jeq       not_prime

forEach_divisor_in_3_to_sqrt_n:
  read_a    n
  sqrt
  write_a   root
  literal_a 3
  write_a   divisor

while_divisor_le_root:
  read_a    divisor
  read_b    root
  cmp
  jgt       is_prime

  read_a    n
  read_b    divisor
  mod
  jeq       not_prime

increment_divisor:
  read_a    divisor
  literal_b 1
  add
  write_a   divisor
  jmp       while_divisor_le_root

is_prime:
  read_a  n
  print_a
  newline

not_prime:
increment_n:
  read_a    n
  literal_b 1
  add
  write_a   n
  jmp       while_n_le_high

done:
  halt

